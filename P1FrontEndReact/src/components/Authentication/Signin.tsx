import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { Button, Form } from "react-bootstrap";
import Loading from "../Utils/Loading";
import SomethingWentWrong from "../Utils/SomethingWentWrong";
import InputField from "../Utils/InputField";
import { BASE_URL } from "../Utils/Config";
function Authentication() {
  const [registeredAccount, setRegisteredAccount] = useState("Sign In");
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    name: "",
    role: "EMPLOYEE",
  });

  const [error, setError] = useState();
  const [isLoading, setIsLoading] = useState(false);
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
    const fetchData = async () => {
      let END_POINT =
        registeredAccount === "Sign Up" ? "/employee" : "/employee/login";
      setIsLoading(true);
      try {
        const response = await fetch(BASE_URL + END_POINT, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body:
            registeredAccount === "Sign Up"
              ? JSON.stringify({
                  name: formData.name,
                  email: formData.email,
                  password: formData.password,
                  role: formData.role,
                })
              : JSON.stringify({
                  email: formData.email,
                  password: formData.password,
                }),
        });
        const data = await response.json();
        if (response.ok) {
          console.log(data);
          if (registeredAccount === "Sign Up") {
            toast.success("User registered successfully");
            setRegisteredAccount("Sign In");
            clearFormData();
          } else {
            toast.success("User logged in successfully");
            if (data.role === "EMPLOYEE") {
              navigate("/employee", { state: { user: data } });
            } else {
              navigate("/manager", { state: { user: data } });
            }
          }
        } else {
          toast.error(data.message);
          console.log(data);
        }
      } catch (error) {
        setError(error as any);
        console.error(error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchData();
  };
  if (isLoading) return <Loading />;
  if (error) return <SomethingWentWrong message={(error as any).message} />;

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
          onClick={handleSubmit}
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
