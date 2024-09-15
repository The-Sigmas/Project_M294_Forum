import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import * as React from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";

// Components
import NavBar from "./components/NavBar.jsx"
import About from "./components/About.jsx"

function App() {
  const [count, setCount] = useState(0)

  return (<BrowserRouter>
            <div>
              <Routes>
                <Route path="/" element={<NavBar />}>
                  <Route index element={<h1 className="bg-gradient-to-r from-red-500 via-red-700 to-red-900 bg-clip-text text-transparent">Welcome to The Forums</h1>} />
                    <Route path="/about" element={<About />} />
                </Route>
              </Routes>
            </div>
          </BrowserRouter>)
}

export default App
