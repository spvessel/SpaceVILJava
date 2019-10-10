using System;
using System.Collections.Generic;
using System.Threading;
using SpaceVIL.Core;
using Glfw3;

namespace SpaceVIL
{
    public static class WindowManager
    {
        private static Object _lock = new Object();
        private static readonly float _intervalVeryLow = 1.0f;
        private static readonly float _intervalLow = 1.0f / 10.0f;
        private static readonly float _intervalMedium = 1.0f / 30.0f;
        private static readonly float _intervalHigh = 1.0f / 60.0f;
        private static readonly float _intervalUltra = 1.0f / 120.0f;
        private static float _intervalAssigned = 1.0f / 10.0f;
        private static RedrawFrequency _frequency = RedrawFrequency.Low;

        public static void SetRenderFrequency(RedrawFrequency value)
        {
            Monitor.Enter(_lock);
            try
            {
                _frequency = value;
                if (value == RedrawFrequency.VeryLow)
                {
                    _intervalAssigned = _intervalVeryLow;
                }
                if (value == RedrawFrequency.Low)
                {
                    _intervalAssigned = _intervalLow;
                }
                else if (value == RedrawFrequency.Medium)
                {
                    _intervalAssigned = _intervalMedium;
                }
                else if (value == RedrawFrequency.High)
                {
                    _intervalAssigned = _intervalHigh;
                }
                else if (value == RedrawFrequency.Ultra)
                {
                    _intervalAssigned = _intervalUltra;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - SetFrequency");
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        private static int _vsync = 1;

        public static void EnableVSync(int value)
        {
            if (_isRunning)
                return;
            _vsync = value;
        }

        public static int GetVSyncValue()
        {
            return _vsync;
        }

        public static void SetRenderType(RenderType value)
        {
            Monitor.Enter(_lock);
            try
            {
                waitfunc = null;
                if (value == RenderType.IfNeeded)
                {
                    waitfunc += () => Glfw.WaitEvents();
                }
                else if (value == RenderType.Periodic)
                {
                    waitfunc += () => Glfw.WaitEventsTimeout(GetCurrentFrequency());
                }
                else if (value == RenderType.Always)
                {
                    waitfunc += () => Glfw.PollEvents();
                }

            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - SetRenderType");
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        private static float GetCurrentFrequency()
        {
            Monitor.Enter(_lock);
            try
            {
                return _intervalAssigned;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - SetFrequency");
                Console.WriteLine(ex.StackTrace);
                return _intervalLow;
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        public static RedrawFrequency GetRenderFrequency()
        {
            Monitor.Enter(_lock);
            try
            {
                return _frequency;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - SetFrequency");
                Console.WriteLine(ex.StackTrace);
                _frequency = RedrawFrequency.Low;
                return _frequency;
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        private static Dictionary<CoreWindow, bool> _initializedWindows = new Dictionary<CoreWindow, bool>();
        private static List<CoreWindow> _windows = new List<CoreWindow>();
        private static Queue<CoreWindow> _listWaitigForInit = new Queue<CoreWindow>();
        private static Queue<CoreWindow> _listWaitigForClose = new Queue<CoreWindow>();
        private static bool _isRunning = false;

        internal static bool IsRunning()
        {
            return _isRunning;
        }

        private static bool _isEmpty = true;

        public static void AddWindow(CoreWindow wnd)
        {
            Monitor.Enter(_lock);
            try
            {
                if (!_windows.Contains(wnd))
                {
                    if (_isRunning)
                    {
                        _listWaitigForInit.Enqueue(wnd);
                    }
                    else
                    {
                        _windows.Add(wnd);
                        _isEmpty = (_windows.Count == 0);
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        private static List<CoreWindow> GetStoredWindows()
        {
            Monitor.Enter(_lock);
            try
            {
                return new List<CoreWindow>(_windows);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        public static void CloseWindow(CoreWindow wnd)
        {
            Monitor.Enter(_lock);
            try
            {
                if (_initializedWindows.ContainsKey(wnd))
                {
                    _listWaitigForClose.Enqueue(wnd);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        private static EventCommonMethod waitfunc;
        static void Run()
        {
            foreach (CoreWindow wnd in _windows)
            {
                InitWindow(wnd);
            }
            if (waitfunc == null)
                waitfunc += () => Glfw.WaitEventsTimeout(GetCurrentFrequency());

            _isRunning = true;
            while (!_isEmpty)
            {
                List<CoreWindow> list = GetStoredWindows();
                waitfunc.Invoke();
                foreach (CoreWindow window in list)
                {
                    Glfw.MakeContextCurrent(window.GetGLWID());
                    window.UpdateScene();
                }
                CoreWindow wnd = WindowsBox.GetCurrentFocusedWindow();
                if (wnd != null && _initializedWindows.ContainsKey(wnd))
                {
                    Glfw.MakeContextCurrent(wnd.GetGLWID());
                }
                VerifyToCloseWindows();
                VerifyToInitWindows();
            }
        }

        private static void VerifyToCloseWindows()
        {
            while (_listWaitigForClose.Count > 0)
            {
                CoreWindow wnd = _listWaitigForClose.Dequeue();
                _windows.Remove(wnd);
                _initializedWindows.Remove(wnd);
                _isEmpty = (_windows.Count == 0);
                Glfw.MakeContextCurrent(wnd.GetGLWID());
                wnd.Dispose();
                wnd.IsClosed = true;
            }
        }

        private static void VerifyToInitWindows()
        {
            while (_listWaitigForInit.Count > 0)
            {
                CoreWindow wnd = _listWaitigForInit.Dequeue();
                InitWindow(wnd);
                _windows.Add(wnd);
            }
        }

        private static void InitWindow(CoreWindow wnd)
        {
            if (!_initializedWindows.ContainsKey(wnd))
            {
                if (wnd.InitEngine())
                {
                    _initializedWindows.Add(wnd, true);
                    wnd.EventOnStart?.Invoke();
                }
            }
        }

        public static void StartWith(params CoreWindow[] windows)
        {
            foreach (CoreWindow wnd in windows)
            {
                AddWindow(wnd);
            }
            Run();
        }

        public static void AppExit()
        {
            Monitor.Enter(_lock);
            try
            {
                _listWaitigForClose = new Queue<CoreWindow>(_windows);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }
    }
}