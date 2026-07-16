import { useNavigate } from "react-router-dom";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import "./back-button.css";

export default function BotaoVoltar({
  className = "",
  onClick,
  ariaLabel = "Voltar",
}) {
  const navigate = useNavigate();
  const classes = ["botao-voltar", className].filter(Boolean).join(" ");

  return (
    <button
      type="button"
      className={classes}
      onClick={onClick || (() => navigate(-1))}
      aria-label={ariaLabel}
    >
      <ArrowBackIosNewIcon />
    </button>
  );
}
