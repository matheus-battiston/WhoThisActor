import "./mobile-segmented-control.css";

export default function MobileSegmentedControl({
  options,
  value,
  onChange,
  className = "",
  size = "default",
}) {
  const classes = [
    "mobile-segmented-control",
    size !== "default" ? `mobile-segmented-control-${size}` : "",
    className,
  ]
    .filter(Boolean)
    .join(" ");

  return (
    <div
      className={classes}
      style={{ "--mobile-segmented-columns": options.length }}
    >
      {options.map((option) => {
        const ativo = option.value === value;
        const Icone = option.Icone;

        return (
          <button
            key={option.value}
            type="button"
            className={`mobile-segmented-control-item ${ativo ? "mobile-segmented-control-item-ativo" : ""}`}
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
