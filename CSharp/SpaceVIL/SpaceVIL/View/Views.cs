﻿using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL;
using System.Threading;

namespace View
{
    #region MaimWindow
    //MainWindow
    partial class MainWindow
    {
        WindowLayout wnd_handler;
        public WindowLayout Handler
        {
            get
            {
                return wnd_handler;
            }
            set
            {
                wnd_handler = value;
            }
        }
        private void InitWindow()
        {
            wnd_handler = new WindowLayout(name: nameof(MainWindow));
            wnd_handler.SetWidth(1000);
            wnd_handler.SetMinWidth(1000);
            wnd_handler.SetHeight(600);
            wnd_handler.SetMinHeight(600);
            wnd_handler.SetWindowTitle(nameof(MainWindow));
            WindowLayoutBox.InitWindow(wnd_handler);
            wnd_handler.SetPadding(60, 100, 60, 100);
            Handler.SetBackground(Color.FromArgb(255, 76, 76, 76));

            //Frame
            Frame frame = new Frame();
            frame.SetBackground(Color.FromArgb(255, 51, 51, 51));
            frame.SetItemName("Container");
            frame.SetWidth(400);
            frame.SetHeight(200);

            frame.SetPadding(100, 30, 30, 30);
            frame.SetWidthPolicy(SizePolicy.Expand);
            frame.SetHeightPolicy(SizePolicy.Expand);
            wnd_handler.AddItem(frame);

            //checkbox
            CheckBox checkBox = new CheckBox();
            checkBox.SetItemName("CheckBox");
            checkBox.SetText("Show window with grid testing.");
            checkBox.SetFont(new Font(new FontFamily("Courier New"), 32, FontStyle.Regular));
            checkBox.SetHeight(40);
            checkBox.SetWidthPolicy(SizePolicy.Expand);
            checkBox.SetAlignment(ItemAlignment.Top | ItemAlignment.HCenter);
            checkBox.EventMouseClick += (sender) =>
            {
                WindowLayoutBox.GetWindowInstance("GridTest")?.Show();
            };
            frame.AddItem(checkBox);
            checkBox.GetIndicator().AddItemState(
                true,
                ItemStateType.Toggled,
                new ItemState() { Background = Color.FromArgb(255, 15, 170, 62) });

            //Button Action
            ButtonCore btn_action = new ButtonCore();

            btn_action.SetBackground(Color.FromArgb(255, 255, 130, 200));
            btn_action.SetText("Layout Test");
            btn_action.SetForeground(Color.Black);
            btn_action.SetItemName("Action");
            btn_action.SetWidth(190);
            btn_action.SetMaxWidth(390);
            btn_action.SetHeight(100);
            btn_action.SetMaxHeight(200);
            btn_action.SetWidthPolicy(SizePolicy.Expand);
            btn_action.SetHeightPolicy(SizePolicy.Expand);
            btn_action.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            btn_action.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            btn_action.Border.Radius = 10;
            btn_action.EventMouseClick += (sender) =>
            {
                WindowLayoutBox.GetWindowInstance("LayoutsTest")?.Show();
                Console.WriteLine(ItemsLayoutBox.GetLayoutItems(wnd_handler.Id).Count);
                WindowLayoutBox.PrintStoredWindows();

            };
            frame.AddItem(btn_action);

            if (true)
                Console.WriteLine(Handler.GetWindowName());

            //Button Settings
            ButtonCore btn_settings = new ButtonCore();
            btn_settings.SetBackground(Color.FromArgb(255, 255, 181, 111));
            btn_settings.SetText("Settings");
            btn_settings.SetForeground(Color.Black);
            btn_settings.SetItemName("Settings");
            btn_settings.SetWidth(190);
            btn_settings.SetHeight(100);
            btn_settings.SetX(100);
            btn_settings.SetY(100);
            btn_settings.SetWidthPolicy(SizePolicy.Fixed);
            btn_settings.SetHeightPolicy(SizePolicy.Fixed);
            btn_settings.SetAlignment(ItemAlignment.Right | ItemAlignment.Bottom);
            btn_settings.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            btn_settings.Border.Radius = 20;
            btn_settings.EventMouseClick += (sender) =>
            {
                WindowLayoutBox.GetWindowInstance("Settings")?.Show();
                Console.WriteLine(btn_settings.GetAlignment());
            };
            frame.AddItem(btn_settings);

            //Button Inputs
            ButtonCore btn_input = new ButtonCore();
            btn_input.SetBackground(100, 175, 255);
            btn_input.SetText("Inputs");
            btn_input.SetForeground(Color.Black);
            btn_input.SetItemName("Inputs");
            btn_input.SetWidth(190);
            btn_input.SetHeight(60);
            btn_input.SetWidthPolicy(SizePolicy.Fixed);
            btn_input.SetHeightPolicy(SizePolicy.Fixed);
            btn_input.SetAlignment(ItemAlignment.Left | ItemAlignment.Bottom);
            btn_input.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            btn_input.Border.Radius = 12;
            btn_input.EventMouseClick += (sender) =>
            {
                WindowLayoutBox.GetWindowInstance("InputTest")?.Show();
                Console.WriteLine(btn_input.GetAlignment());
            };
            frame.AddItem(btn_input);

            //Button Toggle
            ButtonToggle btn_toggle = new ButtonToggle();
            btn_toggle.SetBackground(Color.FromArgb(255, 111, 181, 255));
            btn_toggle.SetItemName("Toggle");
            btn_toggle.SetWidth(50);
            btn_toggle.SetHeight(50);
            btn_toggle.SetWidthPolicy(SizePolicy.Fixed);
            btn_toggle.SetHeightPolicy(SizePolicy.Fixed);
            btn_toggle.SetAlignment(ItemAlignment.Right | ItemAlignment.VCenter);
            btn_toggle.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            btn_toggle.AddItemState(true, ItemStateType.Toggled, new ItemState()
            {
                Background = Color.FromArgb(255, 100, 255, 150)
            });
            btn_toggle.Border.Radius = 20;
            btn_toggle.IsCustom = true;
            btn_toggle.SetTriangles(GraphicsMathService.GetCross(25, 25, 8, 45));

            btn_toggle.EventToggle += (sender) =>
            {
                WindowLayoutBox.GetWindowInstance("ImageTest")?.Show();
            };

            frame.AddItem(btn_toggle);
        }
        public void Show()
        {
            wnd_handler.Show();
        }
    }
    #endregion

    #region GridTest
    //GridTest
    partial class GridTest
    {
        WindowLayout wnd_handler;
        public WindowLayout Handler
        {
            get
            {
                return wnd_handler;
            }
            set
            {
                wnd_handler = value;
            }
        }
        private void InitWindow()
        {
            wnd_handler = new WindowLayout(name: nameof(GridTest));
            WindowLayoutBox.InitWindow(wnd_handler);
            //window's attr
            wnd_handler.SetWindowTitle(nameof(GridTest));
            wnd_handler.SetWidth(500);
            wnd_handler.SetMinWidth(500);
            wnd_handler.SetHeight(500);
            wnd_handler.SetMinHeight(500);
            wnd_handler.SetPadding(2, 2, 2, 2);
            wnd_handler.SetBackground(Color.FromArgb(255, 51, 51, 51));

            //ToolBar
            VerticalStack layout = new VerticalStack();
            layout.SetPadding(6, 6, 6, 6);
            layout.SetSpacing(vertical: 10);
            layout.SetBackground(255, 255, 255, 20);

            //adding toolbar
            wnd_handler.AddItem(layout);

            //Frame
            HorizontalStack toolbar = new HorizontalStack();
            toolbar.SetBackground(Color.FromArgb(255, 100, 100, 100));
            toolbar.SetItemName(nameof(toolbar));
            toolbar.SetHeight(80);
            toolbar.SetPadding(20);
            toolbar.SetSpacing(20);
            toolbar.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            layout.AddItem(toolbar);

            //grid
            Grid grid = new Grid(2, 3);
            grid.SetSpacing(6, 6);
            layout.AddItem(grid);

            for (int i = 0; i < 2; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    ButtonCore rect = new ButtonCore();
                    rect.SetBackground(125, 45, 78);
                    rect.SetItemName("r: " + i + " c: " + j);
                    rect.SetText(rect.GetItemName());
                    rect.SetSize(100, 100);
                    rect.Border.Radius = 20;
                    rect.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
                    rect.SetAlignment(ItemAlignment.Right | ItemAlignment.VCenter);
                    rect.SetMargin(6, 6, 6, 6);
                    rect.AddItemState(true, ItemStateType.Hovered, new ItemState()
                    {
                        Background = Color.FromArgb(125, 255, 255, 255)
                    });
                    rect.EventMouseClick += (sender) =>
                    {
                        Console.WriteLine(rect.GetItemName());
                    };
                    grid.AddItem(rect, i, j);
                }
            }
            //btn add_at_begin
            ButtonCore add_at_begin = new ButtonCore();
            add_at_begin.SetBackground(13, 176, 255);
            add_at_begin.SetItemName(nameof(add_at_begin));
            add_at_begin.Border.Radius = 12;
            add_at_begin.SetWidth(60);
            add_at_begin.SetHeight(60);
            add_at_begin.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            add_at_begin.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            add_at_begin.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            add_at_begin.EventMouseClick += (sender) =>
            {
                Console.WriteLine(
                    "Rows: " + grid.GetRowCount()
                    + "\nColumns: " + grid.GetColumnCount()
                    );
                //
            };

            //btn add_at_center
            ButtonCore add_at_center = new ButtonCore();
            add_at_center.SetBackground(121, 223, 152);
            add_at_center.SetItemName(nameof(add_at_center));
            add_at_center.Border.Radius = 12;
            add_at_center.SetWidth(60);
            add_at_center.SetHeight(60);
            add_at_center.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            add_at_center.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            add_at_center.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            add_at_center.EventMouseClick += (sender) =>
            {
                Console.WriteLine(grid.GetItemName());
                //
            };
            //btn add_at_end
            ButtonCore add_at_end = new ButtonCore();
            add_at_end.SetBackground(238, 174, 128);
            add_at_end.SetItemName(nameof(add_at_end));
            add_at_end.Border.Radius = 12;
            add_at_end.SetWidth(60);
            add_at_end.SetHeight(60);
            add_at_end.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            add_at_end.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            add_at_end.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            add_at_end.EventMouseClick += (sender) =>
            {
                foreach (var item in grid.GetAllCells())
                {
                    item.PrintCellInfo();
                    Console.WriteLine();
                    //item.SetMinHeight(100);
                    //item.SetMinWidth(200);
                }
            };

            //adding buttons
            toolbar.AddItems(add_at_begin, add_at_center, add_at_end);
        }
        public void Show()
        {
            wnd_handler.Show();
        }
    }
    #endregion

    #region  Settings
    //Settings
    partial class Settings
    {
        WindowLayout wnd_handler;
        public WindowLayout Handler
        {
            get
            {
                return wnd_handler;
            }
            set
            {
                wnd_handler = value;
            }
        }
        private void InitWindow()
        {
            int count = 10;
            wnd_handler = new WindowLayout(name: nameof(Settings));
            wnd_handler.SetWidth(600);
            wnd_handler.SetMinWidth(600);
            wnd_handler.SetHeight(550);
            wnd_handler.SetMinHeight(550);
            wnd_handler.SetWindowTitle(nameof(Settings));
            WindowLayoutBox.InitWindow(wnd_handler);
            wnd_handler.SetPadding(30, 50, 30, 50);
            Handler.SetBackground(Color.FromArgb(255, 76, 76, 76));

            CustomShape random_shape = new CustomShape();
            random_shape.SetBackground(Color.FromArgb(255, 200, 51, 220));
            random_shape.SetWidth(50);
            random_shape.SetHeight(50);
            random_shape.SetWidthPolicy(SizePolicy.Fixed);
            random_shape.SetHeightPolicy(SizePolicy.Fixed);
            wnd_handler.AddItem(random_shape);
            random_shape.SetTriangles(GraphicsMathService.GetStar(250, 125, count));

            VerticalStack stack = new VerticalStack();
            stack.SetItemName("Settings_VStack");
            stack.SetBackground(Color.FromArgb(255, 51, 51, 51));
            stack.SetPadding(20, 20, 20, 20);
            stack.SetSpacing(10, 10);
            stack.SetWidth(300);
            stack.SetWidthPolicy(SizePolicy.Fixed);
            stack.SetHeightPolicy(SizePolicy.Expand);
            stack.SetAlignment(ItemAlignment.HCenter);
            wnd_handler.AddItem(stack);

            CheckBox chb = new CheckBox();
            chb.SetItemName("SelfDestructor");
            chb.SetText("Self destructor.");
            chb.SetFont(new Font(new FontFamily("Courier New"), 16, FontStyle.Regular));
            chb.SetWidth(200);
            chb.SetMinWidth(20);
            chb.SetHeight(50);
            chb.SetMinHeight(20);
            chb.SetWidthPolicy(SizePolicy.Fixed);
            chb.SetHeightPolicy(SizePolicy.Fixed);
            chb.SetAlignment(ItemAlignment.HCenter);
            chb.EventMouseClick += (sender) =>
            {
                chb.SetHeight(chb.GetHeight() - 5);
                chb.SetWidth(chb.GetWidth() - 10);
                Console.WriteLine(chb.GetWidth());
            };
            stack.AddItem(chb);

            ButtonCore btn1 = new ButtonCore();
            stack.AddItem(btn1);
            btn1.SetBackground(Color.FromArgb(255, 46, 204, 112));
            btn1.SetItemName("PrintWindows");
            btn1.SetText("Print all windows.");
            btn1.SetForeground(Color.Black);
            btn1.SetWidth(200);
            btn1.SetMinWidth(200);
            btn1.SetHeight(50);
            btn1.SetMinHeight(50);
            btn1.SetAlignment(ItemAlignment.HCenter);
            btn1.AddItemState(true, ItemStateType.Hovered, new ItemState() { Background = Color.FromArgb(125, 255, 255, 255) });
            btn1.SetWidthPolicy(SizePolicy.Fixed);
            btn1.EventMouseClick += (sender) =>
            {
                Console.WriteLine(btn1.GetItemName());
            };

            ButtonCore btn2 = new ButtonCore();
            btn2.SetBackground(Color.FromArgb(255, 46, 112, 204));
            btn2.SetItemName("CloseThis");
            btn2.SetText("Close Settings");
            btn2.SetForeground(Color.Black);
            btn2.SetWidth(200);
            btn2.SetMinWidth(200);
            btn2.SetHeight(50);
            btn2.SetMinHeight(50);
            btn2.AddItemState(true, ItemStateType.Hovered, new ItemState() { Background = Color.FromArgb(125, 255, 255, 255) });
            btn2.SetWidthPolicy(SizePolicy.Expand);
            btn2.SetHeightPolicy(SizePolicy.Expand);
            btn2.EventMouseClick += (sender) =>
            {
                Console.WriteLine(btn2.GetItemName());
                wnd_handler.Close();
            };
            stack.AddItem(btn2);
        }
        public void Show()
        {
            wnd_handler.Show();
        }
    }
    #endregion

    #region  LayoutTest
    //LayoutsTest
    partial class LayoutsTest
    {
        WindowLayout wnd_handler;
        public WindowLayout Handler
        {
            get
            {
                return wnd_handler;
            }
            set
            {
                wnd_handler = value;
            }
        }
        private void InitWindow()
        {
            wnd_handler = new WindowLayout(name: nameof(LayoutsTest));
            WindowLayoutBox.InitWindow(wnd_handler);
            Handler.SetBackground(Color.FromArgb(255, 40, 40, 40));
            wnd_handler.SetWidth(400);
            wnd_handler.SetMinWidth(200);
            wnd_handler.SetHeight(600);
            wnd_handler.SetMinHeight(200);
            wnd_handler.SetWindowTitle("Window");

            //Bar
            VerticalStack btn_bar = new VerticalStack();
            btn_bar.SetBackground(Color.FromArgb(255, 40, 40, 40));
            btn_bar.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            btn_bar.SetItemName("ButtonBar");
            btn_bar.SetWidthPolicy(SizePolicy.Expand);
            btn_bar.SetHeightPolicy(SizePolicy.Expand);
            btn_bar.SetSpacing(10, 10);
            btn_bar.SetPadding(3, 3, 3, 3);
            wnd_handler.AddItem(btn_bar);

            //Frame
            Frame frame = new Frame();
            frame.SetBackground(Color.FromArgb(255, 70, 70, 70));
            frame.SetItemName("Frame");
            frame.SetHeight(40);
            frame.SetPadding(20, 0, 20, 0);
            frame.SetWidthPolicy(SizePolicy.Expand);
            frame.SetHeightPolicy(SizePolicy.Fixed);
            btn_bar.AddItem(frame);

            ListBox listbox = new ListBox();
            listbox.SetBackground(Color.FromArgb(255, 70, 70, 70));
            listbox.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            btn_bar.AddItem(listbox);

            //button
            ButtonCore up = new ButtonCore();
            up.SetBackground(Color.FromArgb(255, 181, 255, 111));
            up.SetItemName("Up");
            up.SetText("Contact");
            up.SetForeground(Color.Black);
            up.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.Bottom);
            up.SetFont(new Font(new FontFamily("Courier New"), 12, FontStyle.Regular));
            up.SetWidth(50);
            up.SetHeight(20);
            up.SetWidthPolicy(SizePolicy.Fixed);
            up.SetHeightPolicy(SizePolicy.Fixed);
            up.SetAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
            up.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            up.Border.Radius = 10;
            up.EventMouseClick += (sender) =>
            {
                Contact.VisualContact contact = new Contact.VisualContact();
                contact.SetAlignment(ItemAlignment.Top | ItemAlignment.Left);
                listbox.AddItem(contact);
            };
            frame.AddItem(up);

            ButtonCore down = new ButtonCore();
            down.SetBackground(Color.FromArgb(255, 181, 111, 255));
            down.SetItemName("Down");
            down.SetText("Show");
            down.SetForeground(Color.Black);
            down.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.Bottom);
            down.SetFont(new Font(new FontFamily("Courier New"), 12, FontStyle.Regular));
            down.SetWidth(50);
            down.SetHeight(20);
            down.SetWidthPolicy(SizePolicy.Fixed);
            down.SetHeightPolicy(SizePolicy.Fixed);
            down.SetAlignment(ItemAlignment.Right | ItemAlignment.VCenter);
            down.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            down.Border.Radius = 10;
            down.EventMouseClick += (sender) =>
            {
                PrintService.PrintList(listbox.GetItems());
            };
            frame.AddItem(down);

            ButtonCore btn = new ButtonCore();
            btn.SetBackground(Color.FromArgb(255, 255, 181, 111));
            btn.SetItemName("Add");
            btn.SetText("Radio");
            btn.SetFont(new Font(new FontFamily("Courier New"), 12, FontStyle.Regular));
            btn.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.Bottom);
            btn.SetForeground(Color.Black);
            btn.SetWidth(50);
            btn.SetHeight(20);
            btn.SetWidthPolicy(SizePolicy.Fixed);
            btn.SetHeightPolicy(SizePolicy.Fixed);
            btn.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            btn.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            btn.Border.Radius = 10;
            btn.EventMouseClick += (sender) =>
            {
                RadioButton radio = new RadioButton();
                radio.SetText("Another radio button for testing.");
                radio.SetFont(new Font(new FontFamily("Courier New"), 16, FontStyle.Regular));
                radio.SetAlignment(ItemAlignment.Top | ItemAlignment.Left);
                radio.SetHeightPolicy(SizePolicy.Fixed);
                radio.SetWidthPolicy(SizePolicy.Expand);
                radio.SetHeight(20);
                radio.EventMouseClick += (_) =>
                {
                    Console.WriteLine(radio.GetItemName());
                };
                listbox.AddItem(radio);
            };
            frame.AddItem(btn);
        }
        public void Show()
        {
            wnd_handler.Show();
        }
    }
    #endregion

    #region  LabelTest
    //LabelTest
    partial class LabelTest
    {
        WindowLayout wnd_handler;
        public WindowLayout Handler
        {
            get
            {
                return wnd_handler;
            }
            set
            {
                wnd_handler = value;
            }
        }
        private void InitWindow()
        {
            wnd_handler = new WindowLayout(name: nameof(LabelTest));
            wnd_handler.SetWidth(500);
            wnd_handler.SetMinWidth(500);
            wnd_handler.SetHeight(500);
            wnd_handler.SetMinHeight(500);
            wnd_handler.SetWindowTitle(nameof(LabelTest));
            WindowLayoutBox.InitWindow(wnd_handler);
            Handler.SetBackground(Color.FromArgb(255, 76, 76, 76));

            //ToolBar
            VerticalStack layout = new VerticalStack();
            layout.SetPadding(3, 3, 3, 3);
            layout.SetSpacing(vertical: 5);
            layout.SetBackground(255, 255, 255, 20);

            //adding toolbar
            wnd_handler.AddItem(layout);

            //Frame
            HorizontalStack toolbar = new HorizontalStack();
            toolbar.SetBackground(Color.FromArgb(255, 60, 60, 60));
            toolbar.SetItemName(nameof(toolbar));
            toolbar.SetHeight(40);
            toolbar.SetPadding(10);
            toolbar.SetSpacing(10);
            toolbar.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            layout.AddItem(toolbar);

            //Frame
            Frame frame = new Frame();
            frame.SetBackground(Color.FromArgb(255, 51, 51, 51));
            frame.SetItemName("Container");
            frame.SetPadding(15, 15, 15, 15);
            frame.SetWidthPolicy(SizePolicy.Expand);
            frame.SetHeightPolicy(SizePolicy.Expand);
            layout.AddItem(frame);

            //ProgressBar
            ProgressBar progb = new ProgressBar();
            progb.SetWidthPolicy(SizePolicy.Expand);
            //progb.SetHeightPolicy(SizePolicy.Expand);
            progb.SetMinValue(35);
            progb.SetMaxValue(78);
            layout.AddItem(progb);

            //BorderShape
            /*BorderElement be = new BorderElement();
            be.SetBackground(Color.Green);
            be.SetWidthPolicy(SizePolicy.Expand);
            be.SetHeight(50);
            layout.AddItem(be);*/

            //Label
            Label label = new Label("Hello world 12345!");
            label.SetFont(new Font(new FontFamily("Times New Roman"), 16, FontStyle.Regular));
            //label.SetFont(new Font(new FontFamily("Panforte Pro"), 16, FontStyle.Regular));
            label.SetForeground(Color.FromArgb(255, 210, 210, 210));
            label.Border.Radius = 10;
            label.SetBackground(Color.FromArgb(10, 255, 255, 255));
            label.SetHeight(50);
            label.SetAlignment(ItemAlignment.VCenter);
            label.SetTextAlignment(ItemAlignment.VCenter | ItemAlignment.HCenter);
            label.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            label.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(60, 255, 255, 255)
            });

            frame.AddItem(label);

            //style
            Style btn_style = new Style();
            btn_style.Background = Color.FromArgb(255, 13, 176, 255);
            btn_style.BorderRadius = 6;
            btn_style.Width = 30;
            btn_style.Height = 30;
            btn_style.WidthPolicy = SizePolicy.Fixed;
            btn_style.HeightPolicy = SizePolicy.Fixed;
            btn_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            ItemState brighter = new ItemState();
            brighter.Background = Color.FromArgb(125, 255, 255, 255);
            btn_style.ItemStates.Add(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });

            //btn add_at_begin
            ButtonCore show_info = new ButtonCore();
            show_info.SetItemName(nameof(show_info));
            show_info.SetStyle(btn_style);
            show_info.EventMouseClick += (sender) =>
            {
                Console.WriteLine(label.GetText());
                label.SetForeground(Color.FromArgb(255, 210, 210, 210));
                label.SetFont(new Font(new FontFamily("Calibri"), 30, FontStyle.Italic));

                progb.SetCurrentValue(33);
            };
            toolbar.AddItem(show_info);

            ButtonCore change_color = new ButtonCore();
            change_color.SetItemName(nameof(show_info));
            change_color.SetStyle(btn_style);
            change_color.EventMouseClick += (sender) =>
            {
                label.SetForeground(Color.FromArgb(255, 50, 255, 80));
                Console.WriteLine(label.GetForeground());

                progb.SetCurrentValue(55);
            };
            toolbar.AddItem(change_color);

            ButtonCore change_font = new ButtonCore();
            change_font.SetItemName(nameof(show_info));
            change_font.SetStyle(btn_style);
            change_font.EventMouseClick += (sender) =>
            {
                label.SetFont(new Font(new FontFamily("Open Sans Light"), 30, FontStyle.Bold));
                Console.WriteLine(label.GetFont());

                //Thread tr = new Thread(() =>
                //{
                for (int i = 35; i <= 78; i++)
                {
                    progb.SetCurrentValue(i);
                    Thread.Sleep(50);
                }
                //});
                //tr.Start();
            };
            toolbar.AddItem(change_font);
        }

        public void Show()
        {
            wnd_handler.Show();
        }
    }
    #endregion

    #region  ImageTest
    //ImageTest
    partial class ImageTest
    {
        WindowLayout wnd_handler;
        public WindowLayout Handler
        {
            get
            {
                return wnd_handler;
            }
            set
            {
                wnd_handler = value;
            }
        }
        private void InitWindow()
        {
            wnd_handler = new WindowLayout(name: nameof(ImageTest));
            wnd_handler.SetWidth(500);
            wnd_handler.SetMinWidth(500);
            wnd_handler.SetHeight(500);
            wnd_handler.SetMinHeight(500);
            wnd_handler.SetWindowTitle(nameof(ImageTest));
            WindowLayoutBox.InitWindow(wnd_handler);
            Handler.SetBackground(Color.FromArgb(255, 76, 76, 76));

            ButtonCore btn_action = new ButtonCore();

            btn_action.SetBackground(100, 255, 150);
            btn_action.SetText("Columnar");
            btn_action.SetTextMargin(new Margin(0, 45, 0, 0));
            btn_action.SetForeground(Color.Black);
            btn_action.SetItemName("Action");
            btn_action.SetWidth(256);
            btn_action.SetHeight(128);
            btn_action.SetWidthPolicy(SizePolicy.Fixed);
            btn_action.SetHeightPolicy(SizePolicy.Fixed);
            btn_action.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            btn_action.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            btn_action.Border.Radius = 10;
            Handler.AddItem(btn_action);

            //Image img1 = Image.FromFile("D:\\icon.png");
            Image img1 = Image.FromFile("D:\\columnar.png");
            //Image img1 = Image.FromFile("D:\\sample.png");
            //Image img1 = Image.FromFile("D:\\icon.jpg");

            ImageItem image = new ImageItem(img1);
            image.SetBackground(Color.Transparent);
            image.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            image.SetAlignment(ItemAlignment.VCenter | ItemAlignment.HCenter);
            btn_action.AddItem(image);
        }

        public void Show()
        {
            wnd_handler.Show();
        }
    }
    #endregion

    #region  InputTest

    partial class InputTest
    {
        WindowLayout wnd_handler;
        public WindowLayout Handler
        {
            get
            {
                return wnd_handler;
            }
            set
            {
                wnd_handler = value;
            }
        }
        private void InitWindow()
        {
            wnd_handler = new WindowLayout(name: nameof(InputTest));
            wnd_handler.SetWidth(500);
            wnd_handler.SetMinWidth(500);
            wnd_handler.SetHeight(200);
            wnd_handler.SetMinHeight(200);
            wnd_handler.SetWindowTitle(nameof(InputTest));
            WindowLayoutBox.InitWindow(wnd_handler);
            Handler.SetBackground(Color.FromArgb(255, 76, 76, 76));

            //ToolBar
            VerticalStack layout = new VerticalStack();
            layout.SetPadding(3, 3, 3, 3);
            layout.SetSpacing(vertical: 5);
            layout.SetBackground(255, 255, 255, 20);

            //adding toolbar
            wnd_handler.AddItem(layout);

            //Frame
            HorizontalStack toolbar = new HorizontalStack();
            toolbar.SetBackground(Color.FromArgb(255, 60, 60, 60));
            toolbar.SetItemName(nameof(toolbar));
            toolbar.SetHeight(40);
            toolbar.SetPadding(10);
            toolbar.SetSpacing(10);
            toolbar.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            layout.AddItem(toolbar);

            //Frame
            Frame frame = new Frame();
            frame.SetBackground(Color.FromArgb(255, 51, 51, 51));
            frame.SetItemName("Container");
            frame.SetPadding(15, 15, 15, 15);
            frame.SetWidthPolicy(SizePolicy.Expand);
            frame.SetHeightPolicy(SizePolicy.Expand);
            layout.AddItem(frame);

            //password input
            PasswordLine pwd = new PasswordLine();
            pwd.Border.Radius = 4;
            pwd.SetHeight(30);
            pwd.SetAlignment(ItemAlignment.VCenter);
            pwd.SetTextAlignment(ItemAlignment.VCenter | ItemAlignment.Left);
            pwd.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            pwd.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(60, 255, 255, 255)
            });
            pwd.SetFont(new Font(new FontFamily("Arial"), 12, FontStyle.Regular));
            frame.AddItem(pwd);

            //text input
            TextEdit text = new TextEdit();
            text.Border.Radius = 4;
            text.SetHeight(30);
            text.SetAlignment(ItemAlignment.Bottom);
            text.SetTextAlignment(ItemAlignment.VCenter | ItemAlignment.Left);
            text.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            text.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(60, 255, 255, 255)
            });
            text.SetFont(new Font(new FontFamily("Courier New"), 16, FontStyle.Regular));

            frame.AddItem(text);

            //style
            Style btn_style = new Style();
            btn_style.Background = Color.FromArgb(255, 13, 176, 255);
            btn_style.BorderRadius = 6;
            btn_style.Width = 30;
            btn_style.Height = 30;
            btn_style.WidthPolicy = SizePolicy.Fixed;
            btn_style.HeightPolicy = SizePolicy.Fixed;
            btn_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            ItemState brighter = new ItemState();
            brighter.Background = Color.FromArgb(125, 255, 255, 255);
            btn_style.ItemStates.Add(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });

            //btn add_at_begin
            ButtonCore show_info = new ButtonCore();
            show_info.SetItemName(nameof(show_info));
            show_info.SetStyle(btn_style);
            show_info.EventMouseClick += (sender) =>
            {
                Console.WriteLine(pwd.GetPassword());
            };
            toolbar.AddItem(show_info);

            ButtonCore change_color = new ButtonCore();
            change_color.SetItemName(nameof(change_color));
            change_color.SetStyle(btn_style);
            change_color.EventMouseClick += (sender) =>
            {

            };
            toolbar.AddItem(change_color);

            ButtonCore change_font = new ButtonCore();
            change_font.SetItemName(nameof(change_font));
            change_font.SetStyle(btn_style);
            change_font.EventMouseClick += (sender) =>
            {

            };
            toolbar.AddItem(change_font);
        }
        public void Show()
        {
            wnd_handler.Show();
        }
    }

    #endregion
}
