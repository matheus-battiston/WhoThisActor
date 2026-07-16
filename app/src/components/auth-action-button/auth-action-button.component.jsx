import LoginOutlinedIcon from "@mui/icons-material/LoginOutlined";
import LogoutOutlinedIcon from "@mui/icons-material/LogoutOutlined";
import { useNavigate } from "react-router-dom";
import { useLogout } from "../../api/hooks/use-logout/use-logout.api";
import { useAuth } from "../../hooks/use-auth/use-auth.hook";
import "./auth-action-button.css";

export default function AuthActionButton({ className = "" }) {
  const navigate = useNavigate();
  const { logout } = useLogout();
  const { isAuthenticated } = useAuth();
  const Icone = isAuthenticated ? LogoutOutlinedIcon : LoginOutlinedIcon;
  const label = isAuthenticated ? "Sair" : "Entrar";

  const handleClick = () => {
    if (isAuthenticated) {
      void logout();
      return;
    }

    navigate("/login");
  };

  return (
    <button
      type="button"
      className={["auth-action-botao", className].filter(Boolean).join(" ")}
      onClick={handleClick}
    >
      <Icone />
      <span>{label}</span>
    </button>
  );
}
