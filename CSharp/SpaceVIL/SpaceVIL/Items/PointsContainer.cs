using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;

namespace SpaceVIL
{
    public class PointsContainer : Primitive, IPoints, ILine
    {
        // PrimitiveType _shape
        static int count = 0;
        public PointsContainer() : base(name: "PointsContainer_" + count)
        {
            SetBackground(Color.Transparent);
            count++;
        }

        float _thickness = 1.0f;
        public void SetPointThickness(float thickness)
        {
            _thickness = thickness;
        }
        public float GetPointThickness()
        {
            return _thickness;
        }
        Color _color = Color.White;
        public void SetPointColor(Color color)
        {
            _color = color;
        }
        public Color GetPointColor()
        {
            return _color;
        }
        List<float[]> _shape_pointer;
        public void SetShapePointer(List<float[]> shape)
        {
            if (shape == null)
                return;
            _shape_pointer = shape;
        }
        public List<float[]> GetShapePointer()
        {
            if (_shape_pointer == null)
                return GraphicsMathService.GetEllipse(GetPointThickness() / 2.0f);
            return _shape_pointer;
        }

        public void SetPointsCoord(List<float[]> coord)
        {
            List<float[]> tmp = new List<float[]>();
            foreach (var item in coord)
            {
                tmp.Add(item);
            }
            SetTriangles(tmp);
        }

        private List<float[]> UpdateCrd()
        {
            //clone triangles
            List<float[]> result = new List<float[]>();
            for (int i = 0; i < GetTriangles().Count; i++)
                result.Add(new float[] { GetTriangles().ElementAt(i)[0], GetTriangles().ElementAt(i)[1], GetTriangles().ElementAt(i)[2] });
            
            //to the left top corner
            foreach (var item in result)
            {
                item[0] = (item[0]) + GetX();
                item[1] = (item[1]) + GetY();
            }

            return result;
        }

        public override List<float[]> MakeShape()
        {
            if (GetTriangles() == null || GetTriangles().Count < 2)
                return null;

            // List<float[]> figure = UpdateShape();
            // List<float[]> result = new List<float[]>();
            // foreach (var shape in figure)
            //     result.AddRange(GraphicsMathService.MoveShape(GetShapePointer(), shape[0], shape[1]));
            // return GraphicsMathService.ToGL(result, GetHandler());
            return UpdateShape();
            // return GraphicsMathService.ToGL(UpdateShape(), GetHandler());
        }

        float _line_thickness = 1.0f;
        public void SetLineThickness(float thickness)
        {
            _line_thickness = thickness;
        }
        public float GetLineThickness()
        {
            return _line_thickness;
        }
        Color _line_color = Color.Blue;
        public void SetLineColor(Color color)
        {
            _line_color = color;
        }
        public Color GetLineColor()
        {
            return _line_color;
        }
    }
}