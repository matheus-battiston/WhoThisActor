import { Navigate } from "react-router-dom";
import Loading from "../components/loading/loading.component";
import { useAuth } from "../hooks/use-auth/use-auth.hook";

export function PrivateRoute({ Screen }) {
  const { isAuthenticated, authChecked } = useAuth();

  if (!authChecked) {
    return <Loading />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Screen />;
}
