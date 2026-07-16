import MovieOutlinedIcon from "@mui/icons-material/MovieOutlined";
import TvOutlinedIcon from "@mui/icons-material/TvOutlined";
import "./media-type-toggle.css";

const OPTIONS = [
  { label: "Filmes", value: "FILME", Icone: MovieOutlinedIcon },
  { label: "TV", value: "TV", Icone: TvOutlinedIcon },
];

export default function MediaTypeToggle({ value, onChange, className = "" }) {
  return (
    <div className={["media-type-toggle", className].filter(Boolean).join(" ")}>
      {OPTIONS.map((option) => {
        const ativo = option.value === value;
        const Icone = option.Icone;

        return (
          <button
            key={option.value}
            type="button"
            className={[
              "media-type-toggle-item",
              ativo ? "media-type-toggle-item-ativo" : "",
            ]
              .filter(Boolean)
              .join(" ")}
            onClick={() => onChange(option.value)}
          >
            <Icone aria-hidden="true" />
            <span>{option.label}</span>
          </button>
        );
      })}
    </div>
  );
}
