import { createBrowserRouter } from "react-router-dom";
import {
  ExibirElencoScreen,
  ExibirProducoesScreen,
  HomeScreen,
  OpcoesAtoresScreen,
  OpcoesProducoesScreen,
} from "../screens/index";
import { LoginScreen } from "../screens/login/login.screen";
import FavoritosScreen from "../screens/favoritos/favoritos.screen";
import { PrivateRoute } from "./private-route.component";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <HomeScreen />,
  },
  {
    path: "/ator/:id",
    element: <ExibirProducoesScreen />,
  },
  {
    path: "/opcoesAtores",
    element: <OpcoesAtoresScreen />,
  },
  {
    path: "/opcoesProducoes",
    element: <OpcoesProducoesScreen />,
  },
  {
    path: "/exibirElenco/:tipo/:id",
    element: <ExibirElencoScreen />,
  },
  {
    path: "/login",
    element: <LoginScreen />,
  },
  {
    path: "/fav",
    element: <PrivateRoute Screen={FavoritosScreen} />,
  },
]);
