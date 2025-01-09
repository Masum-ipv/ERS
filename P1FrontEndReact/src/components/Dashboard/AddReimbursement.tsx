import { Modal, Button, Form } from "react-bootstrap";
import { useState } from "react";
import { toast } from "react-toastify";
import { useAuth } from "../Utils/AuthContext";
import axiosInstance from "../Utils/AxioInstance";
import Loading from "../Utils/Loading";

interface Props {
  showModal: boolean;
  handleClose: () => void;
}

function AddReimbursement({ showModal, handleClose }: Props) {
  const [formData, setFormData] = useState({
    description: "",
    amount: "",
  });
  const { username, userId } = useAuth();
  const [isLoading, setIsLoading] = useState(false);

  const handleInputChange = (e: any) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const clearFormData = () => {
    setFormData({
      description: "",
      amount: "",
    });
  };

  const handleSave = () => {
    console.log("User Data:", formData);
    setIsLoading(true);
    const END_POINT = "/reimbursement";
    const payload = {
      employeeId: userId,
      description: formData.description,
      amount: formData.amount,
    };

    axiosInstance
      .post(END_POINT, payload)
      .then((response) => {
        console.log(response.data);
        toast.success(response.data.message);
      })
      .catch((error) => {
        console.error("Error:", error);
      })
      .finally(() => {
        setIsLoading(false);
        handleClose(); // Close modal after saving
      });
    clearFormData();
  };
  if (isLoading) return <Loading />;

  return (
    <Modal show={showModal} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Add Reimbursement</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group controlId="formName" className="mb-3">
            <Form.Label>Name</Form.Label>
            <Form.Control
              type="text"
              name="name"
              value={username ?? ""}
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
