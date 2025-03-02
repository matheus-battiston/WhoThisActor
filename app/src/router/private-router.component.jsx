import { Navigate } from "react-router-dom";
import { useSelector } from "react-redux";

export function PrivateRoute({ Screen }) {
  const url = useSelector((state) => state.url.url); // Pegando os dados do Redux

  if (!url) {
    return <Navigate to="/" />; // Se não houver dados, redireciona para "/"
  }

  return <Screen />;
}
