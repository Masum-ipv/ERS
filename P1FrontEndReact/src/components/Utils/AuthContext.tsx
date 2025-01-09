import React, { createContext, useState, useEffect, useContext } from "react";
import Cookies from "js-cookie";
import { jwtDecode } from "jwt-decode";

interface AuthContextType {
  token: string;
  username: string | null;
  role: string | null;
  userId: string | null;
  login: (token: string) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [token, setToken] = useState<string>(Cookies.get("jwtToken") || "");
  const [username, setUsername] = useState<string | null>(null);
  const [role, setRole] = useState<string | null>(null);
  const [userId, setUserId] = useState<string | null>(null);

  useEffect(() => {
    // Decode the JWT to extract claims (username, role, userId)
    if (token) {
      const decodedToken: any = jwtDecode(token);
      console.log("Decoded Token:", decodedToken);
      setUsername(decodedToken.sub);
      setRole(decodedToken.role);
      setUserId(decodedToken.userId);
      console.log("Decoded Token UserID:", decodedToken.userId);
      console.log("Decoded Token role:", decodedToken.role);
    }
  }, [token]);

  const login = (newToken: string) => {
    Cookies.set("jwtToken", newToken, { expires: 1 });
    setToken(newToken);
  };

  const logout = () => {
    Cookies.remove("jwtToken");
    setToken("");
    setUsername(null);
    setRole(null);
    setUserId(null);
  };

  return (
    <AuthContext.Provider
      value={{ token, username, role, userId, login, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
};
// Custom hook to access AuthContext
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
