import { useEffect, useState } from "react";
import Loading from "../Utils/Loading";
import { User } from "../Utils/Interface";
import { Button, Container, Table } from "react-bootstrap";
import SomethingWentWrong from "../Utils/SomethingWentWrong";
import axiosInstance from "../Utils/AxioInstance";
import { toast } from "react-toastify";

function DataTable() {
  const [employees, setEmployees] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = () => {
    setIsLoading(true);
    axiosInstance
      .get("/employee")
      .then((response) => {
        setEmployees(response.data.data);
        setError("");
      })
      .catch((error) => {
        setError((error as any).message || "An error occurred");
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  const deleteEmployee = (employeeId: string) => {
    setIsLoading(true);
    axiosInstance
      .delete("/employee/" + employeeId)
      .then((response) => {
        toast.success(response.data.message);
        fetchEmployees();
        setError("");
      })
      .catch((error) => {
        setError((error as any).message || "An error occurred");
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  const updateEmployee = (employee: User) => {
    setIsLoading(true);
    employee.role === "EMPLOYEE"
      ? (employee.role = "MANAGER")
      : (employee.role = "EMPLOYEE");

    axiosInstance
      .put("/employee", employee)
      .then((response) => {
        toast.success(response.data.message);
        fetchEmployees();
        setError("");
      })
      .catch((error) => {
        setError((error as any).message || "An error occurred");
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  if (isLoading) return <Loading />;
  if (error) return <SomethingWentWrong message={(error as any).message} />;

  return (
    <Container
      style={{ maxHeight: "360px", overflowY: "auto" }}
      className="mt-4"
    >
      <Table className="table table-hover table-striped">
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
