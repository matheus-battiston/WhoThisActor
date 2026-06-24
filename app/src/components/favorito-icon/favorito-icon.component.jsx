import { IconButton } from "@mui/material";
import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";

export default function FavoritoIcon({ ativo, onClick, disabled = false }) {
  return (
    <IconButton onClick={() => onClick()} disabled={disabled}>
      {ativo ? (
        <FavoriteIcon color="error" />
      ) : (
        <FavoriteBorderIcon sx={{ color: "white" }} />
      )}
    </IconButton>
  );
}
