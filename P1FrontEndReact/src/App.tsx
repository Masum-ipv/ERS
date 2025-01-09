import { StrictMode } from "react";
import "./App.css";
import NotFoundPage from "./components/Utils/NotFoundPage";
import Employee from "./components/Dashboard/Employee";
import Manager from "./components/Dashboard/Manager";
import Authentication from "./components/Authentication/Signin";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { AuthProvider } from "./components/Utils/AuthContext";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Authentication />,
    errorElement: <NotFoundPage />,
  },
  {
    path: "/employee",
    element: <Employee />,
    errorElement: <NotFoundPage />,
  },
  {
    path: "/manager",
    element: <Manager />,
    errorElement: <NotFoundPage />,
  },
]);

function App() {
  return (
    <AuthProvider>
      <div className="App">
        <h1 className="position-fixed top-0 start-50 translate-middle-x text-center w-100 py-2">
          Employee Reimbursement System
        </h1>
        <StrictMode>
          <RouterProvider router={router} />
        </StrictMode>
        <ToastContainer />
      </div>
    </AuthProvider>
  );
}

export default App;
