import "./background.css";

export function Background({ children }) {
  return (
    <div className="estilo-background">
      <div className="background-grid" />
      {children}
    </div>
  );
}
