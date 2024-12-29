import { Modal, Button, Form } from "react-bootstrap";
import { useState } from "react";
import { toast } from "react-toastify";
import { BASE_URL } from "../Utils/Config";
import displayErrors from "../Utils/FieldErrors";

interface Props {
  userName: string;
  userId: string;
  showModal: boolean;
  handleClose: () => void;
}

function AddReimbursement({ userName, userId, showModal, handleClose }: Props) {
  const [formData, setFormData] = useState({
    description: "",
    amount: "",
  });

  const handleInputChange = (e: any) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSave = () => {
    const fetchData = async () => {
      console.log("User Data:", formData);
      try {
        const response = await fetch(BASE_URL + "/reimbursement", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            employeeId: userId,
            description: formData.description,
            amount: formData.amount,
          }),
        });
        const data = await response.json();
        if (response.ok) {
          console.log(data);
          toast.success("Reimbursement added successfully");
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
    clearFormData();
  };

  const clearFormData = () => {
    setFormData({
      description: "",
      amount: "",
    });
  };

  return (
    <Modal show={showModal} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Add Reimbursement</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group controlId="formName" className="mb-3">
            <Form.Label>Name</Form.Label>
            <Form.Control type="text" name="name" value={userName} readOnly />
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
            <Form.Label>
              Amount<span style={{ color: "red" }}>*</span>
            </Form.Label>
            <Form.Control
              type="number"
              placeholder="Enter Amount"
              name="amount"
              value={formData.amount}
              onChange={handleInputChange}
            />
          </Form.Group>
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

export default AddReimbursement;
