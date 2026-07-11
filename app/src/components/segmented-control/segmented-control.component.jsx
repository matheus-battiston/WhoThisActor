import "./segmented-control.css";

export default function SegmentedControl({
  options,
  value,
  onChange,
  className = "",
  size = "default",
}) {
  const classes = [
    "segmented-control",
    size !== "default" ? `segmented-control-${size}` : "",
    className,
  ]
    .filter(Boolean)
    .join(" ");

  return (
    <div
      className={classes}
      style={{ "--segmented-control-columns": options.length }}
    >
      {options.map((option) => {
        const ativo = option.value === value;
        const Icone = option.Icone;

        return (
          <button
            key={option.value}
            type="button"
            className={`segmented-control-item ${ativo ? "segmented-control-item-ativo" : ""}`}
            onClick={() => onChange(option.value)}
          >
            {Icone && <Icone aria-hidden="true" />}
            <span>{option.label}</span>
          </button>
        );
      })}
    </div>
  );
}
