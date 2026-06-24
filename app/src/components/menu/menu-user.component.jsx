import "./menu-user.css";
import * as React from "react";
import { useState } from "react";
import Button from "@mui/material/Button";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import { useLogout } from "../../api/hooks/use-logout/use-logout.api";
import { useNavigate } from "react-router-dom";

export default function MenuUser({ isAuthenticated }) {
  const navigate = useNavigate();
  const { logout } = useLogout();
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    handleClose();
    void logout();
  };

  const handleLogin = () => {
    handleClose();
    navigate("/login");
  };

  const handleFavoritos = () => {
    handleClose();
    navigate("/fav");
  };

  const menuSx = {
    paper: {
      sx: {
        minWidth: "80px",
        maxWidth: "300px",
        maxHeight: "400px",

        backgroundColor: "rgb(15, 9, 94)",
        backgroundImage: "none",
        backdropFilter: "blur(10px)",
        color: "#fff",

        border: "1px solid #333",
        borderRadius: "8px",
        boxShadow: "0 4px 20px rgba(0,0,0,0.3)",
        overflowY: "auto",
      },
    },
    list: {
      "aria-labelledby": "basic-button",
      sx: {
        padding: "0 0",
      },
    },
  };

  const menuItemSx = {
    fontSize: "0.95rem",
    fontWeight: 400,
    letterSpacing: "0.02em",
    color: "#36BAFE",
    borderBottom: "1px solid #2a2a3a",
    "&:last-child": {
      borderBottom: "none",
    },
    "&:hover": {
      backgroundColor: "#141436",
      color: "#a78bfa",
    },
    "& .MuiListItemIcon-root": {
      color: "inherit",
      minWidth: "32px",
    },
    "&.Mui-disabled": {
      opacity: 0.4,
    },
  };

  return (
    <div className="menu-user">
      <Button
        id="basic-button"
        aria-controls={open ? "basic-menu" : undefined}
        aria-haspopup="true"
        aria-expanded={open ? "true" : undefined}
        onClick={handleClick}
        sx={{
          color: "#36BAFE",
        }}
      >
        Perfil
      </Button>
      <Menu
        id="basic-menu"
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        slotProps={menuSx}
      >
        {isAuthenticated ? (
          [
            <MenuItem key="favoritos" onClick={handleFavoritos} sx={menuItemSx}>
              Favoritos
            </MenuItem>,
            <MenuItem key="logout" onClick={handleLogout} sx={menuItemSx}>
              Logout
            </MenuItem>,
          ]
        ) : (
          <MenuItem key="login" onClick={handleLogin} sx={menuItemSx}>
            Login
          </MenuItem>
        )}
      </Menu>
    </div>
  );
}
