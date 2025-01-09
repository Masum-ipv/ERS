import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { Button, Form } from "react-bootstrap";
import Loading from "../Utils/Loading";
import InputField from "../Utils/InputField";
import displayErrors from "../Utils/FieldErrors";
import { useAuth } from "../Utils/AuthContext";
import axiosInstance from "../Utils/AxioInstance";
import { jwtDecode } from "jwt-decode";

function Authentication() {
  const [registeredAccount, setRegisteredAccount] = useState("Sign In");
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    name: "",
    role: "EMPLOYEE",
  });

  const [isLoading, setIsLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const clearFormData = () => {
    setFormData({
      email: "",
      password: "",
      name: "",
      role: "EMPLOYEE",
    });
  };

  const handleSubmit = () => {
    const END_POINT =
      registeredAccount === "Sign Up"
        ? "/employee/register"
        : "/employee/login";
    const payload =
      registeredAccount === "Sign Up"
        ? {
            name: formData.name,
            email: formData.email,
            password: formData.password,
            role: formData.role,
          }
        : {
            email: formData.email,
            password: formData.password,
          };

    setIsLoading(true);

    axiosInstance
      .post(END_POINT, payload)
      .then((response) => {
        console.log(response.data);
        if (registeredAccount === "Sign Up") {
          toast.success("User registered successfully");
          setRegisteredAccount("Sign In");
          clearFormData();
        } else {
          toast.success("User logged in successfully");
          console.log(response.data.data.token);
          login(response.data.data.token); //Save JWT to context and cookies

          // Decode the token immediately
          const decodedToken: any = jwtDecode(response.data.data.token);
          console.log("Decoded Token:", decodedToken);

          if (decodedToken.role === "EMPLOYEE") {
            navigate("/employee");
          } else {
            navigate("/manager");
          }
        }
      })
      .catch((error) => {
        if (error.response) {
          const jsonResponse = error.response.data;
          if (jsonResponse.fieldErrors == null) {
            toast.error(jsonResponse.message);
          } else {
            displayErrors(jsonResponse);
          }
          console.log(jsonResponse);
        } else {
          toast.error("Something went wrong");
          console.error(error);
        }
      })
      .finally(() => {
        setIsLoading(false);
      });
  };
  if (isLoading) return <Loading />;

  return (
    <div>
      <Form className="form-container" onSubmit={(e) => e.preventDefault()}>
        <h2>{registeredAccount}</h2>

        {registeredAccount === "Sign Up" && (
          <InputField
            id="username"
            name="name"
            label="Username"
            type="text"
            value={formData.name}
            onChange={handleInputChange}
            placeholder="Enter username"
          />
        )}

        <InputField
          id="email"
          name="email"
          label="Email"
          type="email"
          value={formData.email}
          onChange={handleInputChange}
          placeholder="Enter email"
        />

        <InputField
          id="password"
          name="password"
          label="Password"
          type="password"
          value={formData.password}
          onChange={handleInputChange}
          placeholder="Enter password"
        />

        {registeredAccount === "Sign Up" && (
          <fieldset className="w-50">
            <input
              className="form-check-input"
              type="radio"
              name="role"
              id="employeeRadioId"
              value="EMPLOYEE"
              checked={formData.role === "EMPLOYEE"}
              onChange={handleInputChange}
            />
            <label className="form-check-label w-50" htmlFor="employeeRadioId">
              Employee
            </label>
            <input
              className="form-check-input"
              type="radio"
              name="role"
              value="MANAGER"
              id="managerRadioId"
              checked={formData.role === "MANAGER"}
              onChange={handleInputChange}
            />
            <label className="form-check-label" htmlFor="managerRadioId">
              Manager
            </label>
          </fieldset>
        )}

        <br />
        <Button
          type="button"
          variant="primary"
          className="w-50"
          onClick={() => {
            handleSubmit();
          }}
        >
          Submit
        </Button>
        {registeredAccount === "Sign Up" ? (
          <p>
            Already have an account?{" "}
            <a
              id="loginLink"
              onClick={() => {
                setRegisteredAccount("Sign In");
                clearFormData();
              }}
            >
              Login
            </a>
          </p>
        ) : (
          <p>
            Don't have an account?{" "}
            <a
              id="loginLink"
              onClick={() => {
                setRegisteredAccount("Sign Up");
                clearFormData();
              }}
            >
              Register
            </a>
          </p>
        )}
      </Form>
    </div>
  );
}

export default Authentication;
