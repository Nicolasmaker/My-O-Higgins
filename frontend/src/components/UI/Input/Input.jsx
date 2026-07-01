import { forwardRef } from 'react'
import './Input.css'

const Input = forwardRef(function Input({ label, error, className = '', ...props }, ref) {
  const inputClassName = ['ui-input', className].filter(Boolean).join(' ')

  return (
    <label className="ui-field">
      <span className="ui-field__label">{label}</span>
      <input ref={ref} className={inputClassName} {...props} />
      {error ? <span className="ui-field__error">{error}</span> : null}
    </label>
  )
})

export default Input
