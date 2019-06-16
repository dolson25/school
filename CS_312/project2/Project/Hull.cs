using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace _1_convex_hull
{
    class Hull
    {
        List<System.Drawing.PointF> pointList;
        int rightIndex;

        public Hull(List<System.Drawing.PointF> pointList, int rightIndex)
        {
            this.pointList = pointList;
            this.rightIndex = rightIndex;
        }

        public List<System.Drawing.PointF> getList()
        {
            return pointList;
        }

        public int getRightIndex()
        {
            return rightIndex;
        }
    }
}
