import React from "react";
import { Modal, Button, Form } from "react-bootstrap";
import { useState } from "react";
import { ReimbursementInterface } from "../Interfaces/ReimbursementInterface";
import { User } from "../Interfaces/UserInterface";
import { toast } from "react-toastify";
import { BASE_URL } from "../Utils/Config";
import displayErrors from "../Utils/FieldErrors";

interface Props {
  user: User;
  reimbursement: ReimbursementInterface;
  showUpdateModal: boolean;
  handleClose: () => void;
}

function UpdateReimbursement({
  user,
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

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSave = () => {
    const fetchData = async () => {
      console.log("Updated User Data:", formData);

      try {
        const response = await fetch(BASE_URL + "/reimbursement", {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            reimbursementId: formData.reimbursementId,
            description: formData.description,
            amount: formData.amount,
            status: formData.status,
          }),
        });
        const data = await response.json();
        if (response.ok) {
          toast.success("Reimbursement updated successfully");
          handleClose(); // Close modal after saving
        } else {
          if (data.fieldErrors == null) {
            toast.error(data.message);
          } else {
            displayErrors(data);
          }
        }
        console.log(data);
      } catch (error) {
        console.error("Error:", error);
      }
    };
    fetchData();
  };

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
          {user.role === "MANAGER" && (
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
