import { useSelector } from "react-redux";

export function useAuth() {
  const { usuario, isAuthenticated, authChecked } = useSelector(
    (state) => state.auth,
  );

  return {
    usuario,
    isAuthenticated,
    authChecked,
  };
}
