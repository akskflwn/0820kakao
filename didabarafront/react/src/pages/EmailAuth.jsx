import React from 'react'
import { useLocation } from 'react-router-dom'

function EmailAuth() {
    const location = useLocation();
    console.log("로캐이션",location);

  return (
    <div>EmailAuth</div>
  )
}

export default EmailAuth