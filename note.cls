% note.cls
\NeedsTeXFormat{LaTeX2e}
%\ProvidesClass{note}[2025/09/14 Custom Math + CS Notes Class]

% Base class
\LoadClass[12pt]{article}

% Packages
\RequirePackage[utf8]{inputenc}
\RequirePackage[T1]{fontenc}
\RequirePackage{lmodern}
\RequirePackage{amsmath, amssymb, amsthm, mathtools}
\RequirePackage{geometry}
\RequirePackage{xcolor}
\RequirePackage{hyperref}
\RequirePackage{graphicx}
\RequirePackage{listings}
\RequirePackage{environ}
\RequirePackage{enumitem}
\RequirePackage{lastpage}
\RequirePackage{bbm}
\RequirePackage{tikz}

% Page layout
\geometry{a4paper, margin=1in}

% --- Header and Footer Setup ---
\RequirePackage{fancyhdr}

% 1. Define a command to store the name
\newcommand{\studentname}[1]{\def\thestudentname{#1}}
\studentname{Wojciech Malinowski} % Default to empty if not set

% 2. Configure the standard 'fancy' style (pages 2+)
\pagestyle{fancy}
\fancyhf{} % Clear all default headers/footers
\fancyhead[L]{\thestudentname} % Left Header: Name
\fancyhead[R]{\thepage\ / \pageref*{LastPage}}        % Right Header: Page Number
\setlength{\headheight}{15.5pt}

% 3. Redefine the 'plain' style (Page 1 usually uses this)
\fancypagestyle{plain}{
    \fancyhf{}
    \fancyhead[L]{\thestudentname}
    \fancyhead[R]{\thepage}
    \renewcommand{\headrulewidth}{0.4pt} % Adds the line below the header
}

% Hyperlinks
\hypersetup{
    colorlinks=true,
    linkcolor=blue,
    urlcolor=blue,
    citecolor=blue
}

% Safer custom commands for math symbols
\providecommand{\R}{\mathbb{R}}
\providecommand{\Q}{\mathbb{Q}}
\providecommand{\N}{\mathbb{N}}
\providecommand{\Z}{\mathbb{Z}}
\providecommand{\C}{\mathbb{C}}

% Listings for code (CS)
\lstset{
    basicstyle=\ttfamily\small,
    keywordstyle=\color{blue},
    commentstyle=\color{green!50!black},
    stringstyle=\color{red},
    numbers=left,
    numberstyle=\tiny\color{gray},
    stepnumber=1,
    numbersep=5pt,
    frame=tb,
    breaklines=true,
    showstringspaces=false,
    tabsize=4
}

% --- Custom Environments ---

% An exercise environment with a mandatory argument for custom numbering
\NewEnviron{exercise}[1]{
    \par\medskip
    \noindent\textbf{Exercise #1.}\\ % Print the custom number
    \BODY
    \medskip
}

% A solution environment that uses the stored exercise number
\NewEnviron{solution}{
    \par\medskip
    \noindent\textbf{Solution}\\
    \BODY
    \medskip
}

\NewEnviron{zadanie}[1]{
    \par\medskip
    \noindent\textbf{Zadanie #1.}\\ % Print the custom number
    \BODY
    \medskip
}

\NewEnviron{rozwiazanie}{
    \par\medskip
    \noindent\textbf{Rozwiązanie}\\
    \BODY
    \medskip
}

\setlength{\parindent}{0pt}

\DeclareMathOperator{\lin}{lin}
\DeclareMathOperator{\im}{im}