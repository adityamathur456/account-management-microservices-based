"use client"

import { useState, useEffect } from "react"
import { redirect, useRouter } from "next/navigation"
import axios from "axios"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Table, TableBody, TableCell, TableHeader, TableHead, TableRow } from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import { LogOut, User, History, Send, Eye, EyeOff, Shield, CreditCard, TrendingUp, Zap } from "lucide-react"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import {
  AlertDialog,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogAction,
} from "@/components/ui/alert-dialog";
import api from "../api/axios"


export default function BankDashboard() {
  const router = useRouter()
  const [isLoading, setIsLoading] = useState(true)
  const [showBalance, setShowBalance] = useState(true)
  const [transactionAmount, setTransactionAmount] = useState("")
  const [recipientAccount, setRecipientAccount] = useState("")
  const [TransactionType, setTransactionType] = useState("")
  const [activeAccounts, setActiveAccounts] = useState<{ accountNo: string; customerName: string }[]>([])
  const [filteredAccounts, setFilteredAccounts] = useState<{ accountNo: string; customerName: string }[]>([])
  const [customerData, setCustomerData] = useState<any>({
    name: "loding...",
    customerId: "loding...",
    email: "loding...",
    phone: "loding...",
    address: "loding...",
  })
  const [account, setAccount] = useState<any>({
    accountNo: "loding...",
    balance: "loding...",
    accountType: "loding...",
    status: "loding..."
  })
  const [transactions, setTransactions] = useState<any[]>([])
  const [open, setOpen] = useState(false);
  const [transactionResult, setTransactionResult] = useState<{
    status: string;
    remarks: string;
  } | null>(null);

  const customerId = typeof window !== "undefined" ? sessionStorage.getItem("customerId") : null
  const token = typeof window !== "undefined" ? sessionStorage.getItem("token") : null

  // Auth check
  useEffect(() => {
    if (!token || !customerId) {
      router.push("/") // redirect to login
      return
    }

    const fetchData = async () => {
      try {
        setIsLoading(true)
        // Fetch customer details
        const customerRes = await api.get(`/customers/${customerId}`)
        const customer = customerRes.data
        console.log(`${customer.firstName} ${customer.lastName}`)
        setCustomerData({
          name: `${customer.firstName} ${customer.lastName}`,
          customerId: customer.id,
          email: customer.email,
          phone: customer.phoneNumber,
          address: `${customer.address.houseNo}, ${customer.address.street}, ${customer.address.city}, ${customer.address.state} - ${customer.address.pincode}`,
        })
        fetchAccount()
        fetchAllAccounts()
      } catch (err) {
        console.error(err)
      } finally {
        setIsLoading(false)
      }
    }

    fetchData()
  }, [router, token, customerId])

  const handleLogout = () => {
    sessionStorage.clear()
    router.push("/")
  }

  const fetchAllAccounts = async (): Promise<void> => {
    try {
      const { data } = await api.get<{ accountNo: string }[]>(`/accounts`)
      const sourceAccountNo = sessionStorage.getItem("accountNumber")

      const rows = await Promise.all(
        data.map(async (acc) => {
          const { data: details } = await api.get<{
            accountNo: string
            customerName: string
            status: string
          }>(`/accounts/${acc.accountNo}/details`)

          return {
            accountNo: details.accountNo || acc.accountNo,
            customerName: details.customerName,
            status: details.status,
          }
        })
      )

      const activeDest = rows
        .filter(a => a.status === "ACTIVE" && a.accountNo !== sourceAccountNo)
        .map(({ accountNo, customerName }) => ({ accountNo, customerName }))

      setActiveAccounts(activeDest)
      setFilteredAccounts(activeDest)
    } catch (err) {
      console.error("Error fetching accounts:", err)
    }
  }


  const fetchAccount = async () => {
    try {
      const accountsRes = await api.get(`/accounts/AccountDetail/${customerId}`)
      sessionStorage.setItem("accountNumber", accountsRes.data.accountNo)
      const accNo = accountsRes.data.accountNo
      setAccount({
        accountNo: `${accountsRes.data.accountNo}`,
        balance: `${accountsRes.data.balance}`,
        accountType: `${accountsRes.data.accountType}`,
        status: `${accountsRes.data.status}`
      })
      fetchTransaction(accNo)
    } catch (e: any) {
      console.log("Error from fetchAccount")
      console.error(e.response?.data?.message)
    }
  }

  const fetchTransaction = async (accountNumber?: string) => {
    // Fetch transactions for first account
    try {
      const accNo = accountNumber || sessionStorage.getItem("accountNumber")
      const txRes = await api.get(`/transactions/account/${accNo}`)
      setTransactions(txRes.data)
    } catch (e: any) {
      console.log("Error from fetchTransaction")
      console.error(e.response?.data?.message)
    }

  }

  const handleTransaction = async () => {
    if (!transactionAmount || !recipientAccount || !TransactionType) {
      alert("Please fill in all transaction fields")
      return
    }
    const sourceAccount = account.accountNo

    const transactionData = {
      sourceAccountNo: sourceAccount,
      destinationAccountNo: recipientAccount,
      amount: Number(transactionAmount),
      transactionType: TransactionType
    }

    try {
      const res = await api.post("/transactions", transactionData)
      setTransactionResult({
        status: res.data.status,
        remarks: res.data.remarks,
      });

      setOpen(true);
      fetchAccount()
      fetchTransaction()

      // Reset form
      setTransactionAmount("")
      setRecipientAccount("")
      setTransactionType("")
    } catch (err: any) {
      console.error(err)
      if (err.response?.data?.message) {
        setTransactionResult({
          status: "FAILED",
          remarks: err.response?.data?.message,
        });
        setOpen(true);
      } else {
        setTransactionResult({
          status: "Amount not acceptable",
          remarks: "Amount is negative or zero or empty",
        });
        setOpen(true);
      }
    }
  }
  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-blue-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p>Loading your dashboard...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-indigo-50 dark:from-slate-950 dark:via-blue-950 dark:to-indigo-950">
      <header className="border-b bg-white/80 dark:bg-slate-900/80 backdrop-blur-xl shadow-lg">
        <div className="flex h-20 items-center justify-between px-6">
          <div className="flex items-center space-x-4 group">
            <div className="relative">
              <div className="absolute inset-0 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-lg blur opacity-75 group-hover:opacity-100 transition-opacity duration-300"></div>
              <div className="relative bg-gradient-to-r from-blue-600 to-indigo-600 p-2 rounded-lg">
                <Shield className="h-6 w-6 text-white" />
              </div>
            </div>
            <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
              Account Management
            </h1>
          </div>
          <Button
            variant="outline"
            onClick={handleLogout}
            className="flex items-center space-x-2 bg-white/50 dark:bg-slate-800/50 backdrop-blur-sm border-2 hover:bg-red-50 hover:border-red-200 hover:text-red-600 transition-all duration-150 hover:scale-105 hover:shadow-lg"
          >
            <LogOut className="h-4 w-4" />
            <span>Logout</span>
          </Button>
        </div>
      </header>
      <div className="container mx-auto p-6 space-y-8">
        <Card className="bg-white/70 dark:bg-slate-900/70 backdrop-blur-xl border-0 shadow-xl hover:shadow-2xl transition-all duration-200 hover:scale-[1.02] animate-in slide-in-from-top-4 duration-200">
          <CardHeader className="flex flex-row items-center space-y-0 pb-4">
            <div className="flex items-center space-x-3">
              <div className="relative">
                <div className="absolute inset-0 bg-gradient-to-r from-emerald-500 to-teal-500 rounded-full blur opacity-75 animate-pulse"></div>
                <div className="relative bg-gradient-to-r from-emerald-500 to-teal-500 p-2 rounded-full">
                  <User className="h-5 w-5 text-white" />
                </div>
              </div>
              <CardTitle className="text-2xl font-bold text-slate-800 dark:text-slate-100">Customer Details</CardTitle>
            </div>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {[
                { label: "Full Name", value: customerData.name?.toUpperCase() },
                { label: "Customer ID", value: customerData.customerId },
                { label: "Primary Account", value: account.accountNo },
                { label: "Email", value: customerData.email },
                { label: "Phone", value: customerData.phone },
                { label: "Address", value: customerData.address },
              ].map((item, index) => (
                <div
                  key={item.label}
                  className="group p-4 rounded-xl bg-gradient-to-br from-white to-slate-50 dark:from-slate-800 dark:to-slate-900 border border-slate-200 dark:border-slate-700 hover:shadow-lg transition-all duration-100 hover:scale-102"
                  style={{ animationDelay: `${index * 100}ms` }}
                >
                  <Label className="text-sm font-medium text-slate-600 dark:text-slate-400 group-hover:text-emerald-600 dark:group-hover:text-emerald-400 transition-colors">
                    {item.label}
                  </Label>
                  <p className="text-lg font-semibold mt-1 text-slate-900 dark:text-slate-100 group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors">
                    {item.value}
                  </p>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card className="bg-white/70 dark:bg-slate-900/70 backdrop-blur-xl border-0 shadow-xl hover:shadow-2xl transition-all duration-200 hover:scale-[1.02] animate-in slide-in-from-left-4 duration-200 delay-100">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-6">
            <div className="flex items-center space-x-3">
              <div className="relative">
                <div className="absolute inset-0 bg-gradient-to-r from-purple-500 to-pink-500 rounded-full blur opacity-75 animate-pulse"></div>
                <div className="relative bg-gradient-to-r from-purple-500 to-pink-500 p-2 rounded-full">
                  <CreditCard className="h-5 w-5 text-white" />
                </div>
              </div>
              <CardTitle className="text-2xl font-bold text-slate-800 dark:text-slate-100">
                Account Information
              </CardTitle>
            </div>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => setShowBalance(!showBalance)}
              className="flex items-center space-x-2 bg-white/50 dark:bg-slate-800/50 backdrop-blur-sm hover:bg-blue-500 hover:scale-105 transition-all duration-200 rounded-full px-4 py-2"
            >
              {showBalance ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
              <span>{showBalance ? "Hide" : "Show"} Balances</span>
            </Button>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">


              <Card className="md:col-span-3 border-0 bg-gradient-to-br from-emerald-500 to-teal-600 text-white shadow-xl hover:shadow-2xl transition-all duration-200 hover:scale-102 hover:-translate-y-2 group animate-in slide-in-from-bottom-4 duration-200 delay-200">
                <CardHeader className="pb-3">
                  <div className="flex items-center justify-between">
                    <div>
                      <CardTitle className="text-lg text-white/90">{account.accountType} ACCOUNT</CardTitle>
                      <CardDescription className="text-emerald-100">
                        {account.accountNo}
                      </CardDescription>
                    </div>
                    <Zap className="h-6 w-6 text-emerald-200 group-hover:scale-110 transition-transform duration-300" />
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="text-3xl font-bold text-white transition-all duration-500">
                    {showBalance ? `$${account.balance.toLocaleString()}` : "••••••"}
                  </div>
                  <p className="text-sm text-emerald-100 mt-2">Available Balance</p>
                </CardContent>
              </Card>


            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          <Card className="lg:col-span-1 bg-white/70 dark:bg-slate-900/70 backdrop-blur-xl border-0 shadow-xl hover:shadow-2xl transition-all duration-200 animate-in slide-in-from-right-4 duration-200 delay-200">
            <CardHeader className="flex flex-row items-center space-y-0 pb-4">
              <div className="flex items-center space-x-3">
                <div className="relative">
                  <div className="absolute inset-0 bg-gradient-to-r from-indigo-500 to-purple-500 rounded-full blur opacity-75 animate-pulse"></div>
                  <div className="relative bg-gradient-to-r from-indigo-500 to-purple-500 p-2 rounded-full">
                    <History className="h-5 w-5 text-white" />
                  </div>
                </div>
                <CardTitle className="text-xl font-bold text-slate-800 dark:text-slate-100">
                  Recent Transactions
                </CardTitle>
              </div>
            </CardHeader>
            <CardContent>
              <div className="rounded-xl overflow-hidden border border-slate-200 dark:border-slate-700">
                <Table>
                  <TableHeader>
                    <TableRow className="bg-gradient-to-r from-slate-50 to-slate-100 dark:from-slate-800 dark:to-slate-900 hover:from-slate-100 hover:to-slate-200 dark:hover:from-slate-700 dark:hover:to-slate-800">
                      <TableHead className="font-semibold">Time</TableHead>
                      <TableHead className="font-semibold">Type</TableHead>
                      <TableHead className="font-semibold">From</TableHead>
                      <TableHead className="font-semibold">To</TableHead>
                      <TableHead className="font-semibold">Remarks</TableHead>
                      <TableHead className="font-semibold">Amount</TableHead>
                      <TableHead className="font-semibold">Status</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {transactions.map((transaction, index) => (
                      <TableRow
                        key={transaction.transactionId}
                        className="hover:bg-gradient-to-r hover:from-blue-50 hover:to-indigo-50 dark:hover:from-blue-950 dark:hover:to-indigo-950 transition-all duration-200 hover:scale-[1.02] group"
                        style={{ animationDelay: `${index * 100}ms` }}
                      >
                        <TableCell className="font-medium group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors">
                          {transaction.transactionTime}
                        </TableCell>
                        <TableCell className="max-w-[200px] truncate group-hover:text-slate-700 dark:group-hover:text-slate-300 transition-colors">
                          {TransactionType}
                        </TableCell>

                        <TableCell className="max-w-[200px] truncate group-hover:text-slate-700 dark:group-hover:text-slate-300 transition-colors">
                          {transaction.sourceAccountNo}
                        </TableCell>
                        <TableCell className="max-w-[200px] truncate group-hover:text-slate-700 dark:group-hover:text-slate-300 transition-colors">
                          {transaction.destinationAccountNo}
                        </TableCell>
                        <TableCell className="max-w-[200px] truncate group-hover:text-slate-700 dark:group-hover:text-slate-300 transition-colors">
                          {transaction.remarks}
                        </TableCell>

                        <TableCell
                          className={`font-semibold transition-all duration-300 ${transaction.amount > 0 && transaction.status === "SUCCESS"
                            ? "text-emerald-600 group-hover:text-emerald-700"
                            : "text-red-600 group-hover:text-red-700"}`}
                        >
                          {transaction.amount > 0 ? "-" : ""}${Math.abs(transaction.amount).toFixed(2)}
                        </TableCell>
                        <TableCell>
                          <Badge
                            variant={transaction.status === "SUCCESS" ? "default" : "secondary"}
                            className={`transition-all duration-300 ${transaction.status === "SUCCESS"
                              ? "bg-gradient-to-r from-emerald-500 to-teal-500 hover:from-emerald-700 hover:to-teal-700"
                              : "bg-gradient-to-r from-red-500 to-orange-500 hover:from-amber-700 hover:to-orange-600"
                              } text-white border-0 group-hover:scale-110`}
                          >
                            {transaction.status}
                          </Badge>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </div>
            </CardContent>
          </Card>

          <Card className="lg:col-span-1 bg-white/70 dark:bg-slate-900/70 backdrop-blur-xl border-0 shadow-xl hover:shadow-2xl transition-all duration-200 animate-in slide-in-from-left-4 duration-200 delay-200">
            <CardHeader className="flex flex-row items-center space-y-0 pb-4">
              <div className="flex items-center space-x-3">
                <div className="relative">
                  <div className="absolute inset-0 bg-gradient-to-r from-cyan-500 to-blue-500 rounded-full blur opacity-75 animate-pulse"></div>
                  <div className="relative bg-gradient-to-r from-cyan-500 to-blue-500 p-2 rounded-full">
                    <Send className="h-5 w-5 text-white" />
                  </div>
                </div>
                <CardTitle className="text-xl font-bold text-slate-800 dark:text-slate-100">Make Transaction</CardTitle>
                <AlertDialog open={open} onOpenChange={setOpen}>
                  <AlertDialogContent className="bg-white/90 dark:bg-slate-900/90 backdrop-blur-xl shadow-2xl rounded-2xl">
                    <AlertDialogHeader>
                      <AlertDialogTitle className="text-xl font-bold">
                        Transaction - {transactionResult?.status}
                      </AlertDialogTitle>
                      <AlertDialogDescription className="text-slate-600 dark:text-slate-300 mt-2">
                        {transactionResult?.remarks}
                      </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                      <AlertDialogAction
                        className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg"
                        onClick={() => setOpen(false)}
                      >
                        OK
                      </AlertDialogAction>
                    </AlertDialogFooter>
                  </AlertDialogContent>
                </AlertDialog>
              </div>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-3 group">
                <Label
                  htmlFor="transaction-type"
                  className="text-sm font-medium text-slate-700 dark:text-slate-300 group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors"
                >
                  Transaction Type
                </Label>
                <Select value={TransactionType} onValueChange={setTransactionType}>
                  <SelectTrigger className="bg-white/50 dark:bg-slate-800/50 backdrop-blur-sm border-2 hover:border-blue-300 focus:border-blue-500 transition-all duration-300 hover:shadow-lg">
                    <SelectValue placeholder="Select transaction type" />
                  </SelectTrigger>
                  <SelectContent className="bg-white/90 dark:bg-slate-900/90 backdrop-blur-xl border-0 shadow-2xl">
                    <SelectItem value="TRANSFER" className="hover:bg-blue-50 dark:hover:bg-blue-950 transition-colors">
                      TRANSFER
                    </SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="space-y-3 group">
                <Label
                  htmlFor="amount"
                  className="text-sm font-medium text-slate-700 dark:text-slate-300 group-hover:text-emerald-600 dark:group-hover:text-emerald-400 transition-colors"
                >
                  Amount
                </Label>
                <Input
                  id="amount"
                  type="number"
                  placeholder="0.00"
                  value={transactionAmount}
                  onChange={(e) => setTransactionAmount(e.target.value)}
                  className="bg-white/50 dark:bg-slate-800/50 backdrop-blur-sm border-2 hover:border-emerald-300 focus:border-emerald-500 transition-all duration-300 hover:shadow-lg text-lg"
                />
              </div>

              <div className="space-y-3 group">
                <Label
                  htmlFor="recipient"
                  className="text-sm font-medium text-slate-700 dark:text-slate-300 group-hover:text-purple-600 dark:group-hover:text-purple-400 transition-colors"
                >
                  Recipient Account
                </Label>
                {/* <Input
                  id="recipient"
                  placeholder="Enter account number or email"
                  value={recipientAccount}
                  onChange={(e) => setRecipientAccount(e.target.value)}
                  className="bg-white/50 dark:bg-slate-800/50 backdrop-blur-sm border-2 hover:border-purple-300 focus:border-purple-500 transition-all duration-300 hover:shadow-lg"
                /> */}
                <Select value={recipientAccount} onValueChange={setRecipientAccount}>
                  <SelectTrigger className="w-full bg-white/50 dark:bg-slate-800/50 backdrop-blur-sm border-2 hover:border-purple-300 focus:border-purple-500 transition-all duration-300 hover:shadow-lg">
                    <SelectValue placeholder="Select recipient account" />
                  </SelectTrigger>
                  <SelectContent className="bg-white/90 dark:bg-slate-900/90 backdrop-blur-xl border-0 shadow-2xl">
                    {filteredAccounts.map((acc) => (
                      <SelectItem
                        key={acc.accountNo}
                        value={acc.accountNo}
                        className="hover:bg-purple-50 dark:hover:bg-purple-950 transition-colors"
                      >
                        {acc.customerName} ({acc.accountNo})
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>

              <Separator className="bg-gradient-to-r from-transparent via-slate-300 to-transparent dark:via-slate-600" />

              <Button
                onClick={handleTransaction}
                className="w-full bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white border-0 shadow-xl hover:shadow-2xl transition-all duration-300 hover:scale-102 hover:-translate-y-1 text-lg py-6"
                size="lg"
              >
                <Send className="h-5 w-5 mr-2" />
                Process Transaction
              </Button>

              <p className="text-sm text-slate-500 dark:text-slate-400 text-center leading-relaxed">
                After transaction you can see the transaction details in recent transaction
              </p>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
}
