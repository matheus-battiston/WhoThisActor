import { useRef } from "react";
import "./pillToggle.css";

export function PillToggle({ options, value, onChange }) {
  const startX = useRef(0);
  const activeIndex = options.findIndex((opt) => opt.value === value);

  const handleTouchStart = (e) => {
    startX.current = e.touches[0].clientX;
  };

  const handleTouchEnd = (e) => {
    const endX = e.changedTouches[0].clientX;
    const diff = endX - startX.current;
    const threshold = 40;

    if (Math.abs(diff) < threshold) return;

    if (diff > 0 && activeIndex > 0) {
      onChange(options[activeIndex - 1].value);
    } else if (diff < 0 && activeIndex < options.length - 1) {
      onChange(options[activeIndex + 1].value);
    }
  };

  return (
    <div
      className="pill-container"
      onTouchStart={handleTouchStart}
      onTouchEnd={handleTouchEnd}
    >
      <div
        className="pill-slider"
        style={{
          width: `calc((100% - 8px) / ${options.length})`,
          transform: `translateX(${activeIndex * 100}%)`,
        }}
      />

      {options.map((option) => (
        <button
          key={option.value}
          className={`pill-item ${value === option.value ? "ativo" : ""}`}
          onClick={() => onChange(option.value)}
        >
          {option.label}
        </button>
      ))}
    </div>
  );
}
