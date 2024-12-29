import { toast } from "react-toastify";

const displayErrors = (data: any) => {
  // Display the formatted error messages in a toast
  toast.error(
    <div>
      <ul style={{ listStyleType: "none", paddingLeft: "0" }}>
        {data.fieldErrors.map((error: any, index: any) => (
          <li key={index} style={{ marginBottom: "5px" }}>
            <span style={{ marginRight: "5px" }}>•</span>
            {error.message}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default displayErrors;
