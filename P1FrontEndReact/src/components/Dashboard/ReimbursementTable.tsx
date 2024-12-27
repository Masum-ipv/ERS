import { useEffect, useState } from "react";
import { ReimbursementInterface } from "../Interfaces/ReimbursementInterface";
import { Button, Container, Table } from "react-bootstrap";
import { User } from "../Interfaces/UserInterface";
import UpdateReimbursement from "./UpdateReimbursement";
import Loading from "../Utils/Loading";
import SomethingWentWrong from "../Utils/SomethingWentWrong";
import { BASE_URL } from "../Utils/Config";

interface UserProps {
  user: User;
  refreshKey: number;
}

function DataTable({ user, refreshKey }: UserProps) {
  let END_POINT = "";
  const [reimbersements, setReimbursements] = useState<
    ReimbursementInterface[]
  >([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const [allReimbursement, setAllReimbursement] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [refreshKeyForUpdate, setRefreshKeyForUpdate] = useState(0);
  const [selectedReimbursement, setSelectedReimbursement] =
    useState<ReimbursementInterface>();

  useEffect(() => {
    fetchReimbursements();
  }, [allReimbursement, refreshKey, refreshKeyForUpdate]);

  const fetchReimbursements = async () => {
    if (allReimbursement) {
      END_POINT =
        user.role === "EMPLOYEE" ? "/employee/" + user.employeeId : "";
    } else {
      END_POINT =
        user.role === "EMPLOYEE" ? "/PENDING/" + user.employeeId : "/PENDING";
    }
    setIsLoading(true);
    try {
      const response = await fetch(BASE_URL + "/reimbursement" + END_POINT);
      const data = await response.json();
      setReimbursements(data);
      setError("");
    } catch (error) {
      setError((error as any).message || "An error occurred");
    } finally {
      setIsLoading(false);
    }
  };

  const deleteReimbursement = async (id: string) => {
    setIsLoading(true);
    try {
      await fetch(BASE_URL + "/" + id, {
        method: "DELETE",
      });
      fetchReimbursements();
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
    <div className="container mt-4">
      <div className="container d-flex justify-content-end">
        <div className="toggle-switch">
          <input
            type="checkbox"
            className="checkbox"
            name="all_reimbursements"
            id="all_reimbursements"
            checked={allReimbursement}
            onChange={() => setAllReimbursement(!allReimbursement)}
          />
          <label htmlFor="all_reimbursements" className="ms-2">
            All Reimbursements
          </label>
        </div>
      </div>

      <Container style={{ maxHeight: "350px", overflowY: "auto" }}>
        <Table className="table table-hover table-striped">
          <thead className="thead-dark">
            <tr>
              <th>Reimbesment ID</th>
              <th>Employee Name</th>
              <th>Description</th>
              <th>Amount</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {reimbersements.map((reimbersement, index) => (
              <tr key={reimbersement.reimbursementId}>
                <td>{reimbersement.reimbursementId}</td>
                <td>{reimbersement.employee.name}</td>
                <td>{reimbersement.description}</td>
                <td>{reimbersement.amount}</td>
                <td>{reimbersement.status}</td>
                <td>
                  {reimbersement.status === "PENDING" && (
                    <Button
                      className="btn btn-primary me-2"
                      onClick={() => {
                        console.log(`Edit row ${reimbersement.amount}`);
                        setSelectedReimbursement(reimbersement);
                        setShowUpdateModal(true);
                      }}
                    >
                      Edit
                    </Button>
                  )}
                  <Button
                    className="btn btn-danger"
                    onClick={() => {
                      console.log(
                        `Delete Reimbersement ${reimbersement.reimbursementId}`
                      );
                      deleteReimbursement(reimbersement.reimbursementId);
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
      <UpdateReimbursement
        user={user}
        showUpdateModal={showUpdateModal}
        reimbursement={selectedReimbursement as ReimbursementInterface}
        handleClose={() => {
          setRefreshKeyForUpdate(
            (refreshKeyForUpdate) => refreshKeyForUpdate + 1
          );
          setSelectedReimbursement(undefined);
          setShowUpdateModal(false);
        }}
      />
    </div>
  );
}

export default DataTable;
