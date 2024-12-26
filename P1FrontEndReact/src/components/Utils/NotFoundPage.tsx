import { Link } from "react-router-dom";

export default function NotFoundPage() {
  return (
    <div className="text-center">
      <h3>Something went wrong!</h3>
      <Link to="/">Back to Home Page</Link>
    </div>
  );
}
