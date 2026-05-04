import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { hello } from "../../user/hello/hello.api";
import {
  clearAuth,
  setAuthChecked,
  setIsAuthenticated,
  setUsuario,
} from "../../../redux/store";

export function useBootstrapAuth() {
  const dispatch = useDispatch();

  useEffect(() => {
    async function bootstrap() {
      const minDelayPromise = new Promise((resolve) => {
        setTimeout(resolve, 1000);
      });

      try {
        const respostaApiPromise = hello();

        const [respostaApi] = await Promise.all([
          respostaApiPromise,
          minDelayPromise,
        ]);

        dispatch(setUsuario(respostaApi));
        dispatch(setIsAuthenticated(true));
      } catch (error) {
        await minDelayPromise;

        if (error.response?.status === 401) {
          dispatch(clearAuth());
          dispatch(setIsAuthenticated(false));
          dispatch(setUsuario(null));
        } else {
          console.error("Erro ao carregar usuário no bootstrap", error);
        }
      } finally {
        dispatch(setAuthChecked(true));
      }
    }

    void bootstrap();
  }, [dispatch]);
}
