import "./Profile.css";
import NotFoundPage from "../Utils/NotFoundPage";
import ReimbursementTable from "./ReimbursementTable";
import { useState } from "react";
import EmployeeTable from "./EmployeeTable";
import { Navbar, Nav, NavDropdown } from "react-bootstrap";
import AddReimbursement from "./AddReimbursement";
import { useAuth } from "../Utils/AuthContext";

function Profile() {
  const [activeTab, setActiveTab] = useState("reimbursement");
  const [showModal, setShowModal] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);
  const { username, logout } = useAuth();

  const handleSelect = (selectedKey: string | null) => {
    if (selectedKey) setActiveTab(selectedKey); // Update the active tab
  };
  console.log(`Manager: user ${username}`);

  const handleLogout = () => {
    logout();
    window.location.href = "/";
  };

  return (
    <>
      {username ? (
        <div className="profile-container container d-flex">
          <div className="container d-flex justify-content-end">
            <Navbar bg="light" expand="lg">
              <Navbar.Toggle aria-controls="basic-navbar-nav" />
              <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="ml-auto">
                  <NavDropdown
                    title={username}
                    id="user-profile-dropdown"
                    align="end"
                  >
                    <NavDropdown.Item onClick={handleLogout}>
                      Logout
                    </NavDropdown.Item>
                  </NavDropdown>
                </Nav>
              </Navbar.Collapse>
            </Navbar>
          </div>

          <Nav
            variant="tabs"
            activeKey={activeTab} // Control the active tab
            onSelect={handleSelect} // Handle tab selection without updating the URL
          >
            <Nav.Item>
              <Nav.Link eventKey="reimbursement">Reimbursement</Nav.Link>
            </Nav.Item>
            <Nav.Item>
              <Nav.Link eventKey="employees">Employees</Nav.Link>
            </Nav.Item>
          </Nav>
          {activeTab === "reimbursement" && (
            <>
              <ReimbursementTable refreshKey={refreshKey} />
              <button
                type="button"
                className="btn btn-primary w-50 align-self-center mt-5"
                onClick={() => {
                  console.log("clicked to add reimbursement from manager");
                  setShowModal(true);
                }}
              >
                Add Reimbursement
              </button>
            </>
          )}

          {activeTab !== "reimbursement" && (
            <>
              <EmployeeTable />
            </>
          )}

          <AddReimbursement
            showModal={showModal}
            handleClose={() => {
              setRefreshKey((refreshKey) => refreshKey + 1);
              setShowModal(false);
            }}
          />
        </div>
      ) : (
        <NotFoundPage />
      )}
    </>
  );
}

export default Profile;
