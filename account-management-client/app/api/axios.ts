import axios, { InternalAxiosRequestConfig } from "axios";

// Create main API instance
export const api = axios.create({
  baseURL: "http://34.60.148.162:8085/api",
  headers: { "Content-Type": "application/json" },
});

// Create auth API instance
export const authApi = axios.create({
  baseURL: "http://34.60.148.162:8085/auth",
  headers: { "Content-Type": "application/json" },
});

// Interceptor to attach token if available
api.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    if (typeof window !== "undefined") {
      const token = sessionStorage.getItem("token");
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default api;
