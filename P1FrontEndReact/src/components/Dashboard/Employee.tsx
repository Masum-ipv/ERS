import "./Profile.css";
import NotFoundPage from "../Utils/NotFoundPage";
import ReimbursementTable from "./ReimbursementTable";
import { Navbar, Nav, NavDropdown } from "react-bootstrap";
import { useState } from "react";
import AddUpdateReimbursement from "./AddReimbursement";
import { useAuth } from "../Utils/AuthContext";

function Profile() {
  const [showModal, setShowModal] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);
  const { username, logout } = useAuth();

  console.log(`Employee: user ${username}`);
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

          <ReimbursementTable refreshKey={refreshKey} />
          <button
            type="button"
            className="btn btn-primary w-50 align-self-center mt-5"
            onClick={() => {
              console.log("clicked inside profile");
              setShowModal(true);
            }}
          >
            Add Reimbursement
          </button>
          <AddUpdateReimbursement
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
