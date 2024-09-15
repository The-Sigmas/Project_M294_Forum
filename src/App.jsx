import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import * as React from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";

// Components
import NavBar from "./components/NavBar.jsx"

function App() {
  const [count, setCount] = useState(0)

  return (<BrowserRouter>
            <div>
              <Routes>
                <Route path="/" element={<NavBar />}>
                  <Route index element={<h1>All Forums</h1>}>
              </Routes>
            </div>
            <p>Hi</p>
          </BrowserRouter>)
}

export default App
