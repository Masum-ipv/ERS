import React from "react";

interface InputFieldProps {
  id: string;
  label: string;
  type: string;
  name: string;
  value: string;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  placeholder?: string;
  required?: boolean;
  className?: string;
}

const InputField: React.FC<InputFieldProps> = ({
  id,
  label,
  type,
  name,
  value,
  onChange,
  placeholder,
  required = true,
  className = "form-control",
}) => (
  <div className="mb-3 w-50">
    <label htmlFor={id} className="form-label">
      {label}
      {required && <span style={{ color: "red" }}>*</span>}
    </label>
    <input
      type={type}
      className={className}
      id={id}
      name={name}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      required={required}
    />
  </div>
);

export default InputField;
