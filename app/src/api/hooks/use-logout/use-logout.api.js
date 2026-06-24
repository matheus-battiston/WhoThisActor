import { useDispatch } from "react-redux";
import { clearAuth } from "../../../redux/store";
import { logoutApi } from "../../user/logout/perform-logout";

export function useLogout() {
  const dispatch = useDispatch();
  const ERRO_NO_LOGOUT = "Erro no logout";

  async function logout() {
    try {
      await logoutApi();
    } catch (error) {
      console.error(ERRO_NO_LOGOUT, error);
    } finally {
      dispatch(clearAuth());
    }
  }

  return { logout };
}
