import FavoriteBorderOutlinedIcon from "@mui/icons-material/FavoriteBorderOutlined";
import HomeOutlinedIcon from "@mui/icons-material/HomeOutlined";
import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";
import LoginOutlinedIcon from "@mui/icons-material/LoginOutlined";
import LogoutOutlinedIcon from "@mui/icons-material/LogoutOutlined";
import SearchOutlinedIcon from "@mui/icons-material/SearchOutlined";
import { useLocation, useNavigate } from "react-router-dom";
import { useLogout } from "../../api/hooks/use-logout/use-logout.api";
import { useAuth } from "../../hooks/use-auth/use-auth.hook";
import CameraActionCard from "../camera-action-card/camera-action-card.component";
import HeaderLogoButton from "../header-logo-button/header-logo-button.component";
import "./web-side-menu.css";

const itensMenu = [
  {
    label: "Home",
    path: "/",
    Icone: HomeOutlinedIcon,
    exact: true,
  },
  {
    label: "Buscar",
    path: "/buscar",
    Icone: SearchOutlinedIcon,
  },
  {
    label: "Favoritos",
    path: "/fav",
    Icone: FavoriteBorderOutlinedIcon,
  },
  {
    label: "Sobre",
    path: "/sobre",
    Icone: InfoOutlinedIcon,
  },
];

function itemAtivo(item, pathname) {
  if (item.exact) return pathname === item.path;

  return pathname.startsWith(item.path);
}

export default function WebSideMenu() {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const { isAuthenticated } = useAuth();
  const { logout } = useLogout();
  const AuthIcone = isAuthenticated ? LogoutOutlinedIcon : LoginOutlinedIcon;

  function handleAuthClick() {
    if (isAuthenticated) {
      void logout();
      return;
    }

    navigate("/login");
  }

  return (
    <aside className="web-side-menu" aria-label="Navegacao principal">
      <HeaderLogoButton className="web-side-menu-logo" />

      <nav className="web-side-menu-nav">
        {itensMenu.map((item) => {
          const ativo = itemAtivo(item, pathname);
          const Icone = item.Icone;
          const className = [
            "web-side-menu-item",
            ativo ? "web-side-menu-item-ativo" : "",
          ]
            .filter(Boolean)
            .join(" ");

          return (
            <button
              key={item.label}
              type="button"
              className={className}
              onClick={() => navigate(item.path)}
              aria-current={ativo ? "page" : undefined}
            >
              <Icone />
              <span>{item.label}</span>
            </button>
          );
        })}

        <button
          type="button"
          className="web-side-menu-item"
          onClick={handleAuthClick}
        >
          <AuthIcone />
          <span>{isAuthenticated ? "Logout" : "Login"}</span>
        </button>
      </nav>

      <div className="web-side-menu-camera">
        <CameraActionCard
          className="web-side-menu-camera-card"
          titulo="Câmera"
          descricao="Identifique o ator por foto."
        />
      </div>
    </aside>
  );
}
