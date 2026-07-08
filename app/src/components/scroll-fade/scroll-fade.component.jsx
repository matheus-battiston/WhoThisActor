import "./scroll-fade.css";

export default function ScrollFade({ className = "" }) {
  return (
    <div
      className={`scroll-fade ${className}`.trim()}
      aria-hidden="true"
    />
  );
}
