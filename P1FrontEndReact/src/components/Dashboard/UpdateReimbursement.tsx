import React from "react";
import { Modal, Button, Form } from "react-bootstrap";
import { useState } from "react";
import { ReimbursementInterface } from "../Utils/Interface";
import { toast } from "react-toastify";
import { useAuth } from "../Utils/AuthContext";
import axiosInstance from "../Utils/AxioInstance";
import Loading from "../Utils/Loading";

interface Props {
  reimbursement: ReimbursementInterface;
  showUpdateModal: boolean;
  handleClose: () => void;
}

function UpdateReimbursement({
  reimbursement,
  showUpdateModal,
  handleClose,
}: Props) {
  if (!reimbursement) return null;
  const [formData, setFormData] = useState({
    reimbursementId: reimbursement.reimbursementId,
    description: reimbursement.description,
    amount: reimbursement.amount,
    status: reimbursement.status,
  });
  const { role } = useAuth();
  const [isLoading, setIsLoading] = useState(false);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSave = () => {
    console.log("User Data:", formData);
    const END_POINT = "/reimbursement";
    const payload = {
      reimbursementId: formData.reimbursementId,
      description: formData.description,
      amount: formData.amount,
      status: formData.status,
    };
    setIsLoading(true);

    axiosInstance
      .put(END_POINT, payload)
      .then((response) => {
        console.log(response.data);
        toast.success(response.data.message);
        handleClose(); // Close modal after saving
      })
      .catch((error) => {
        console.error("Error:", error);
      })
      .finally(() => {
        setIsLoading(false);
      });
  };
  if (isLoading) return <Loading />;

  return (
    <Modal show={showUpdateModal} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Update Reimbursement</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group controlId="formId" className="mb-3">
            <Form.Label>Reimbersement Id</Form.Label>
            <Form.Control
              type="text"
              name="reimbursementId"
              value={formData.reimbursementId}
              readOnly
            />
          </Form.Group>

          <Form.Group controlId="formDescription" className="mb-3">
            <Form.Label>
              Description<span style={{ color: "red" }}>*</span>
            </Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter description"
              name="description"
              value={formData.description}
              onChange={handleInputChange}
            />
          </Form.Group>

          <Form.Group controlId="formAmount" className="mb-3">
            <Form.Label>Amount</Form.Label>
            <Form.Control
              type="number"
              name="amount"
              value={formData.amount}
              readOnly
            />
          </Form.Group>
          {role === "MANAGER" && (
            <Form.Group controlId="formStatus">
              <Form.Label>Status</Form.Label>
              <Form.Control
                as="select"
                name="status"
                onChange={handleInputChange}
              >
                <option value="">Select status</option>
                <option value="APPROVED">Approved</option>
                <option value="DENIED">Denied</option>
              </Form.Control>
            </Form.Group>
          )}
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={handleSave}>
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default UpdateReimbursement;
