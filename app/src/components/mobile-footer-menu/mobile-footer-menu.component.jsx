import { useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import CameraAltOutlinedIcon from "@mui/icons-material/CameraAltOutlined";
import FavoriteBorderOutlinedIcon from "@mui/icons-material/FavoriteBorderOutlined";
import HomeOutlinedIcon from "@mui/icons-material/HomeOutlined";
import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";
import SearchOutlinedIcon from "@mui/icons-material/SearchOutlined";
import { useCameraCapture } from "../camera-capture/camera-capture.context";
import "./mobile-footer-menu.css";

const itensMenu = [
  {
    label: "Inicio",
    path: "/",
    Icone: HomeOutlinedIcon,
    exact: true,
  },
  {
    label: "Buscar",
    path: "/buscar",
    Icone: SearchOutlinedIcon,
  },
  {
    label: "Camera",
    path: "/",
    Icone: CameraAltOutlinedIcon,
    destaque: true,
  },
  {
    label: "Favoritos",
    path: "/fav",
    Icone: FavoriteBorderOutlinedIcon,
  },
  {
    label: "Sobre",
    path: "/sobre",
    Icone: InfoOutlinedIcon,
  },
];

function itemAtivo(item, pathname) {
  if (item.destaque) return false;
  if (item.exact) return pathname === item.path;

  return pathname.startsWith(item.path);
}

export default function MobileFooterMenu() {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const { abrirCamera } = useCameraCapture();
  const [oculto, setOculto] = useState(false);
  const posicoesScroll = useRef(new WeakMap());

  useEffect(() => {
    setOculto(false);
    posicoesScroll.current = new WeakMap();
  }, [pathname]);

  useEffect(() => {
    function obterDadosScroll(target) {
      if (target === document || target === window) {
        const elemento = document.scrollingElement || document.documentElement;

        return {
          elemento,
          scrollTop: window.scrollY || elemento.scrollTop,
          scrollHeight: elemento.scrollHeight,
          clientHeight: window.innerHeight,
        };
      }

      if (!(target instanceof Element)) return null;

      return {
        elemento: target,
        scrollTop: target.scrollTop,
        scrollHeight: target.scrollHeight,
        clientHeight: target.clientHeight,
      };
    }

    function aoScroll(event) {
      const dados = obterDadosScroll(event.target);

      if (!dados || dados.scrollHeight <= dados.clientHeight) return;

      const ultimoScrollTop = posicoesScroll.current.get(dados.elemento) ?? 0;
      const diferenca = dados.scrollTop - ultimoScrollTop;
      const distanciaDoFim =
        dados.scrollHeight - dados.clientHeight - dados.scrollTop;

      if (dados.scrollTop <= 8) {
        setOculto(false);
        posicoesScroll.current.set(dados.elemento, dados.scrollTop);
        return;
      }

      if (Math.abs(diferenca) < 4) return;

      if (distanciaDoFim <= 12 && diferenca < 0) {
        posicoesScroll.current.set(dados.elemento, dados.scrollTop);
        return;
      }

      setOculto(diferenca > 0);
      posicoesScroll.current.set(dados.elemento, dados.scrollTop);
    }

    document.addEventListener("scroll", aoScroll, {
      capture: true,
      passive: true,
    });

    return () => {
      document.removeEventListener("scroll", aoScroll, true);
    };
  }, []);

  const navClassName = [
    "mobile-footer-menu",
    oculto ? "mobile-footer-menu-oculto" : "",
  ]
    .filter(Boolean)
    .join(" ");

  return (
    <nav className={navClassName} aria-label="Navegacao principal">
      <div className="mobile-footer-menu-interno">
        {itensMenu.map(({ label, path, Icone, destaque, exact }) => {
          const ativo = itemAtivo({ path, destaque, exact }, pathname);
          const className = [
            "mobile-footer-menu-item",
            ativo ? "mobile-footer-menu-item-ativo" : "",
            destaque ? "mobile-footer-menu-item-destaque" : "",
          ]
            .filter(Boolean)
            .join(" ");

          return (
            <button
              key={label}
              type="button"
              className={className}
              onClick={() => {
                if (destaque) {
                  abrirCamera();
                  return;
                }

                navigate(path);
              }}
              aria-label={label}
              aria-current={ativo ? "page" : undefined}
            >
              <span className="mobile-footer-menu-icon">
                <Icone />
              </span>
              <span className="mobile-footer-menu-label">{label}</span>
            </button>
          );
        })}
      </div>
    </nav>
  );
}
