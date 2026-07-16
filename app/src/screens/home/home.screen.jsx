import React from "react";
import { useAuth } from "../../hooks/use-auth/use-auth.hook";
import HomeMobileLayout from "./layouts/home-mobile.layout";

export function HomeScreen() {
  const { authChecked } = useAuth();

  return <HomeMobileLayout authChecked={authChecked} />;
}
