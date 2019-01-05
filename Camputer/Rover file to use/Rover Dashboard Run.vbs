Set oShell = CreateObject ("Wscript.Shell") 
Dim strArgs
strArgs = "cmd /c RoverDashboard.bat"
oShell.Run strArgs, 0, false
