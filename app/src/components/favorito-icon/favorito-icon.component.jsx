import { IconButton } from "@mui/material";
import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import "./favorito-icon.css";

export default function FavoritoIcon({
  ativo,
  onClick,
  disabled = false,
  className = "",
}) {
  const classes = ["favorito-icon", ativo ? "favorito-icon-ativo" : "", className]
    .filter(Boolean)
    .join(" ");

  return (
    <IconButton
      className={classes}
      onClick={onClick}
      disabled={disabled}
      aria-label={ativo ? "Remover dos favoritos" : "Adicionar aos favoritos"}
    >
      {ativo ? (
        <FavoriteIcon />
      ) : (
        <FavoriteBorderIcon />
      )}
    </IconButton>
  );
}
