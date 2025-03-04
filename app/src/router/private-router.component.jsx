import { Navigate } from "react-router-dom";
import { useSelector } from "react-redux";

export function PrivateRoute({ Screen }) {
  const url = useSelector((state) => state.url.url);

  if (!url) {
    return <Navigate to="/" />;
  }

  return <Screen />;
}
