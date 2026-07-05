"use client"

import type React from "react"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { CreditCard, DollarSign, User, CheckCircle, AlertCircle } from "lucide-react"
import Link from "next/link"
import { useRouter } from "next/navigation"
import api from "../api/axios"

export default function AccountRegister() {
  const router = useRouter()
  const [customerId, setCustomerId] = useState("")
  const [balance, setBalance] = useState("")
  const [accountType, setAccountType] = useState("SAVINGS")
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState("")
  const [success, setSuccess] = useState("")

  
  useEffect(() => {
    const userName = sessionStorage.getItem("userName")
    const token = sessionStorage.getItem("token")

    if(!userName || !token){
        router.push("/register")
        return
    }
    // Get customerId from sessionStorage
    const storedCustomerId = sessionStorage.getItem("customerId")
    if (storedCustomerId) {
      setCustomerId(storedCustomerId)
    } else {
      setError("Customer ID not found. Please complete customer registration first.")
    }
  }, [router])

  const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault()
  setError("")
  setSuccess("")
  setIsLoading(true)

  if (!customerId) {
    setError("Customer ID is required")
    setIsLoading(false)
    return
  }

  if (!balance || Number.parseFloat(balance) < 100) {
    setError("Minimum initial balance is $100")
    setIsLoading(false)
    return
  }

  try {
    const res = await api.post("/accounts", {
      customerId,
      balance: Number.parseFloat(balance),
      accountType,
    })

    // Save account number locally if needed
    sessionStorage.setItem("accountNumber", res.data.accountNo)

    setSuccess(`Account created successfully! Account Number: ${res.data.accountNo}`)

    setTimeout(() => {
      router.push("/dashboard")
    }, 1000)
  } catch (err: any) {
    // Axios error handling
    if (err.response?.data?.message) {
      setError(err.response.data.message)
    } else {
      setError("Failed to create account. Please try again.")
    }
  } finally {
    setIsLoading(false)
  }
}

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 flex items-center justify-center p-4">
      <div className="w-full max-w-md space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex justify-center">
            <div className="p-3 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-full">
              <CreditCard className="h-8 w-8 text-white" />
            </div>
          </div>
          <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
            Create Account
          </h1>
          <p className="text-slate-600">Set up your new bank account</p>
        </div>

        {/* Account Registration Form */}
        <Card className="backdrop-blur-sm bg-white/90 border-0 shadow-xl">
          <CardHeader className="space-y-1 pb-4">
            <CardTitle className="text-xl text-slate-800">Account Details</CardTitle>
            <CardDescription className="text-slate-600">Enter your account information below</CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              {/* Customer ID (Read-only) */}
              <div className="space-y-2">
                <Label htmlFor="customerId" className="text-slate-700 font-medium">
                  Customer ID
                </Label>
                <div className="relative">
                  <User className="absolute left-3 top-3 h-4 w-4 text-slate-400" />
                  <Input
                    id="customerId"
                    type="text"
                    value={customerId}
                    readOnly
                    className="pl-10 bg-slate-50 text-slate-600 cursor-not-allowed"
                    placeholder="Customer ID will be loaded automatically"
                  />
                </div>
              </div>

              {/* Initial Balance */}
              <div className="space-y-2">
                <Label htmlFor="balance" className="text-slate-700 font-medium">
                  Initial Balance ($)
                </Label>
                <div className="relative">
                  <DollarSign className="absolute left-3 top-3 h-4 w-4 text-slate-400" />
                  <Input
                    id="balance"
                    type="number"
                    step="0.01"
                    min="100"
                    value={balance}
                    onChange={(e) => setBalance(e.target.value)}
                    className="pl-10 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="Enter initial balance (min $100)"
                    required
                  />
                </div>
              </div>

              {/* Account Type */}
              <div className="space-y-2">
                <Label htmlFor="accountType" className="text-slate-700 font-medium">
                  Account Type
                </Label>
                <Select value={accountType} onValueChange={setAccountType}>
                  <SelectTrigger className="focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                    <SelectValue placeholder="Select account type" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="SAVINGS">Savings Account</SelectItem>
                    <SelectItem value="CURRENT">Current Account</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              {/* Error Alert */}
              {error && (
                <Alert className="border-red-200 bg-red-50">
                  <AlertCircle className="h-4 w-4 text-red-600" />
                  <AlertDescription className="text-red-700">{error}</AlertDescription>
                </Alert>
              )}

              {/* Success Alert */}
              {success && (
                <Alert className="border-green-200 bg-green-50">
                  <CheckCircle className="h-4 w-4 text-green-600" />
                  <AlertDescription className="text-green-700">{success}</AlertDescription>
                </Alert>
              )}

              {/* Submit Button */}
              <button
                type="submit"
                disabled={isLoading || !customerId}
                className="w-full py-3 px-4 rounded-lg font-semibold text-white transition-all duration-200 transform hover:scale-[1.02] active:scale-[0.98] disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
                style={{
                  background: isLoading
                    ? "linear-gradient(135deg, #94a3b8 0%, #64748b 100%)"
                    : "linear-gradient(135deg, #1e40af 0%, #3b82f6 50%, #6366f1 100%)",
                  boxShadow: "0 4px 15px rgba(59, 130, 246, 0.4)",
                }}
              >
                {isLoading ? (
                  <span className="flex items-center justify-center space-x-2">
                    <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                    <span className="text-white font-semibold">Creating Account...</span>
                  </span>
                ) : (
                  <span className="text-white font-semibold">Create Account</span>
                )}
              </button>
            </form>

            {/* Navigation Links */}
            <div className="mt-6 text-center space-y-2">
              <p className="text-sm text-slate-600">
                Need to register as a customer first?{" "}
                <Link
                  href="/register"
                  className="text-blue-600 hover:text-blue-700 font-medium hover:underline transition-colors"
                >
                  Customer Registration
                </Link>
              </p>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
