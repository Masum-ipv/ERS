import { useEffect, useState } from "react";
import { ReimbursementInterface } from "../Utils/Interface";
import { Button, Container, Table } from "react-bootstrap";
import UpdateReimbursement from "./UpdateReimbursement";
import Loading from "../Utils/Loading";
import SomethingWentWrong from "../Utils/SomethingWentWrong";
import axiosInstance from "../Utils/AxioInstance";
import { useAuth } from "../Utils/AuthContext";
import { toast } from "react-toastify";

interface UserProps {
  refreshKey: number;
}

function DataTable({ refreshKey }: UserProps) {
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
  const { role, userId } = useAuth();

  useEffect(() => {
    fetchReimbursements();
  }, [allReimbursement, refreshKey, refreshKeyForUpdate]);

  const fetchReimbursements = () => {
    if (allReimbursement) {
      END_POINT = role === "EMPLOYEE" ? "/employee/" + userId : "";
    } else {
      END_POINT = role === "EMPLOYEE" ? "/PENDING/" + userId : "/PENDING";
    }
    setIsLoading(true);

    axiosInstance
      .get("/reimbursement" + END_POINT)
      .then((response) => {
        console.log(response.data);
        setReimbursements(response.data.data);
        setError("");
      })
      .catch((error) => {
        setError((error as any).message || "An error occurred");
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  const deleteReimbursement = (id: string) => {
    setIsLoading(true);

    axiosInstance
      .delete("/reimbursement/" + id)
      .then((response) => {
        toast.success(response.data.message);
        fetchReimbursements();
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
