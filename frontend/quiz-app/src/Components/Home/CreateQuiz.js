import React, { useState } from 'react'

export default function CreateQuiz() {
    const [mode,setMode] = useState(null)
    return (
        <div className="create-quiz">

            {/* option selection */}
            {mode === null && (
                <div className="quiz-type">
                    <button onClick={() => setMode("manual")}>Manual Question Creation</button>
                    <button onClick={() => setMode("auto")}>Automatic Generation</button>
                </div>
            )}

            {/* manual question creation */}
            {mode === "manual" && (
                <div className="manual-section">
                    <h2>Manual Question Creation</h2>

                    <input type="text" placeholder="Enter question" />
                    <input type="text" placeholder="Option A" />
                    <input type="text" placeholder="Option B" />
                    <input type="text" placeholder="Option C" />
                    <input type="text" placeholder="Option D" />

                    <select>
                        <option value="">Select Correct Answer</option>
                        <option value="A">A</option>
                        <option value="B">B</option>
                        <option value="C">C</option>
                        <option value="D">D</option>
                    </select>

                    <button>Save Question</button>
                </div>
            )}

            {/* automatic question generation */}
            {mode === "auto" && (
                <div className="auto-section">
                    <h2>Automatic Quiz Generation</h2>

                    <input type="text" placeholder="Enter topic" />
                    <input type="number" placeholder="No. of questions" />

                    <select>
                        <option value="">Difficulty Level</option>
                        <option value="easy">Easy</option>
                        <option value="medium">Medium</option>
                        <option value="hard">Hard</option>
                    </select>

                    <button>Generate Quiz</button>
                </div>
            )}

        </div>

    )
}
