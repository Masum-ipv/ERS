import { useEffect, useState } from "react";
import Loading from "../Utils/Loading";
import { User } from "../Interfaces/UserInterface";
import { Button, Container, Table } from "react-bootstrap";
import SomethingWentWrong from "../Utils/SomethingWentWrong";

const BASE_URL = "http://127.0.0.1:8080/employee";

function DataTable() {
  const [employees, setEmployees] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    setIsLoading(true);
    try {
      const response = await fetch(BASE_URL);
      const data = await response.json();
      setEmployees(data);
      setError("");
    } catch (error) {
      setError((error as any).message || "An error occurred");
    } finally {
      setIsLoading(false);
    }
  };

  const deleteEmployee = async (employeeId: string) => {
    setIsLoading(true);
    try {
      await fetch(BASE_URL + "/" + employeeId, {
        method: "DELETE",
      });
      fetchEmployees();
      setError("");
    } catch (error) {
      setError((error as any).message || "An error occurred");
    } finally {
      setIsLoading(false);
    }
  };

  const updateEmployee = async (employee: User) => {
    setIsLoading(true);
    employee.role === "EMPLOYEE"
      ? (employee.role = "MANAGER")
      : (employee.role = "EMPLOYEE");
    try {
      await fetch(BASE_URL, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          employeeId: employee.employeeId,
          name: employee.name,
          email: employee.email,
          role: employee.role,
          password: employee.password,
        }),
      });
      fetchEmployees();
      setError("");
    } catch (error) {
      setError((error as any).message || "An error occurred");
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) return <Loading />;
  if (error) return <SomethingWentWrong message={(error as any).message} />;

  return (
    <Container
      style={{ maxHeight: "360px", overflowY: "auto" }}
      className="mt-4"
    >
      <Table className="table-primary table-hover">
        <thead className="thead-dark">
          <tr>
            <th>Employee ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {employees.map((employee, index) => (
            <tr key={employee.employeeId}>
              <td>{employee.employeeId}</td>
              <td>{employee.name}</td>
              <td>{employee.email}</td>
              <td>{employee.role}</td>
              <td>
                <Button
                  className={`btn me-2 ${
                    employee.role === "EMPLOYEE" ? "btn-success" : "btn-primary"
                  }`}
                  onClick={() => {
                    console.log(`Update Employee ${employee.name}`);
                    updateEmployee(employee);
                  }}
                >
                  {employee.role === "EMPLOYEE" ? "Promote" : "Demote"}
                </Button>
                <Button
                  className="btn btn-danger"
                  onClick={() => {
                    console.log(`Delete Employee ${employee.name}`);
                    deleteEmployee(employee.employeeId);
                  }}
                >
                  Delete
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  );
}

export default DataTable;
