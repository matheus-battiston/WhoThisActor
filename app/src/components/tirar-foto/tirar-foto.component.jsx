import CameraComponent from "../camera/camera.component";
import "./tirar-foto.css";
import { useState } from "react";
import FiltroFavoritos from "../filtro-favoritos/filtro-favoritos.component";

const TirarFoto = ({ isAuthenticated, setLoading, setFraseLoading }) => {
  const [filtrarPorFavoritos, setFiltrarPorFavoritos] = useState(true);

  return (
    <div className="tirar-foto-container">
      <div className="tirar-foto-filtros">
        {isAuthenticated ? (
          <FiltroFavoritos
            ativo={filtrarPorFavoritos}
            onChange={setFiltrarPorFavoritos}
          />
        ) : null}
      </div>
      <CameraComponent
        filtrarPorFavoritos={filtrarPorFavoritos}
        setLoading={setLoading}
        setFraseLoading={setFraseLoading}
      />
    </div>
  );
};

export default TirarFoto;
