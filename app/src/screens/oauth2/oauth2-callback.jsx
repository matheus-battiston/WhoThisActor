import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { setToken } from "../../redux/store";

export default function OAuth2Callback() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");

    if (!token) {
      navigate("/", { replace: true });
      return;
    }

    dispatch(setToken(token));
    window.history.replaceState({}, document.title, "/oauth-success");
    navigate("/", { replace: true });
  }, [navigate, dispatch]);

  return <div>Carregando...</div>;
}
