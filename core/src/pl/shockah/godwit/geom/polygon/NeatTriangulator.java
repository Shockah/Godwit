package pl.shockah.godwit.geom.polygon;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.MutableVec2;

/*
 * code taken from Slick2D - http://slick.ninjacave.com/
 */
public class NeatTriangulator implements Triangulator {
	private static final float EPSILON = 1E-006F;

	private float pointsX[], pointsY[];
	private int numPoints;
	private Edge edges[];
	private int V[];
	private int numEdges;
	private Triangle triangles[];
	private int numTriangles;
	private float offset = EPSILON;

	public NeatTriangulator() {
		pointsX = new float[100];
		pointsY = new float[100];
		numPoints = 0;
		edges = new Edge[100];
		numEdges = 0;
		triangles = new Triangle[100];
		numTriangles = 0;
	}

	public void clear() {
		numPoints = 0;
		numEdges = 0;
		numTriangles = 0;
	}

	private int findEdge(int i, int j) {
		int k, l;
		if (i < j) {
			k = i;
			l = j;
		} else {
			k = j;
			l = i;
		}
		for (int i1 = 0; i1 < numEdges; i1++)
			if (edges[i1].v0 == k && edges[i1].v1 == l)
				return i1;
		return -1;
	}

	private void addEdge(int i, int j, int k) {
		int l1 = findEdge(i, j);
		int j1, k1;
		Edge edge;
		if (l1 < 0) {
			if (numEdges == edges.length) {
				Edge aedge[] = new Edge[edges.length * 2];
				System.arraycopy(edges, 0, aedge, 0, numEdges);
				edges = aedge;
			}
			j1 = k1 = -1;
			l1 = numEdges++;
			edge = edges[l1] = new Edge();
		} else {
			edge = edges[l1];
			j1 = edge.t0;
			k1 = edge.t1;
		}
		int l, i1;
		if (i < j) {
			l = i;
			i1 = j;
			j1 = k;
		} else {
			l = j;
			i1 = i;
			k1 = k;
		}
		edge.v0 = l;
		edge.v1 = i1;
		edge.t0 = j1;
		edge.t1 = k1;
		edge.suspect = true;
	}

	void markSuspect(int i, int j, boolean flag) throws InternalException {
		int k;
		if (0 > (k = findEdge(i, j))) {
			throw new InternalException("Attempt to mark unknown edge");
		} else {
			edges[k].suspect = flag;
		}
	}

	private static boolean insideTriangle(float f, float f1, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f8 = f4 - f2;
		float f9 = f5 - f3;
		float f10 = f - f4;
		float f11 = f1 - f5;
		float f12 = f2 - f;
		float f13 = f3 - f1;
		float f14 = f6 - f;
		float f15 = f7 - f1;
		float f16 = f6 - f2;
		float f17 = f7 - f3;
		float f18 = f6 - f4;
		float f19 = f7 - f5;
		float f22 = f8 * f17 - f9 * f16;
		float f20 = f12 * f15 - f13 * f14;
		float f21 = f10 * f19 - f11 * f18;
		return f22 >= 0f && f21 >= 0f && f20 >= 0f;
	}

	private boolean snip(int i, int j, int k, int l) {
		float f = pointsX[V[i]];
		float f1 = pointsY[V[i]];
		float f2 = pointsX[V[j]];
		float f3 = pointsY[V[j]];
		float f4 = pointsX[V[k]];
		float f5 = pointsY[V[k]];
		if (EPSILON > (f2 - f) * (f5 - f1) - (f3 - f1) * (f4 - f))
			return false;
		for (int i1 = 0; i1 < l; i1++) {
			if(i1 != i && i1 != j && i1 != k) {
				float f6 = pointsX[V[i1]];
				float f7 = pointsY[V[i1]];
				if (insideTriangle(f, f1, f2, f3, f4, f5, f6, f7))
					return false;
			}
		}
		return true;
	}

	private float area() {
		float f = 0f;
		int i = numPoints-1;
		for (int j = 0; j < numPoints;) {
			f += pointsX[i] * pointsY[j] - pointsY[i] * pointsX[j];
			i = j++;
		}
		return f * .5f;
	}

	public void basicTriangulation() throws InternalException {
		int i = numPoints;
		if (i < 3)
			return;
		numEdges = 0;
		numTriangles = 0;
		V = new int[i];

		if (0d < area()) {
			for (int k = 0; k < i; k++)
				V[k] = k;
		} else {
			for (int l = 0; l < i; l++)
				V[l] = numPoints - 1 - l;
		}
		int k1 = 2 * i;
		int i1 = i - 1;
		while (i > 2)  {
			if (0 >= k1--)
				throw new InternalException("Bad polygon");

			int j = i1;
			if (i <= j)
				j = 0;
			i1 = j + 1;
			if (i <= i1)
				i1 = 0;
			int j1 = i1 + 1;
			if (i <= j1)
				j1 = 0;
			if (snip(j, i1, j1, i)) {
				int l1 = V[j];
				int i2 = V[i1];
				int j2 = V[j1];
				if (numTriangles == triangles.length) {
					Triangle atriangle[] = new Triangle[triangles.length * 2];
					System.arraycopy(triangles, 0, atriangle, 0, numTriangles);
					triangles = atriangle;
				}
				triangles[numTriangles] = new Triangle(l1, i2, j2);
				addEdge(l1, i2, numTriangles);
				addEdge(i2, j2, numTriangles);
				addEdge(j2, l1, numTriangles);
				numTriangles++;
				int k2 = i1;
				for (int l2 = i1 + 1; l2 < i; l2++) {
					V[k2] = V[l2];
					k2++;
				}

				i--;
				k1 = 2 * i;
			}
		}
		V = null;
	}

	@Override
	public boolean triangulate() {
		try {
			basicTriangulation();
			return true;
		} catch (InternalException e) {
			numEdges = 0;
		}
		return false;
	}

	@Override
	public void addPoint(@Nonnull IVec2 point) {
		for (int i = 0; i < numPoints; i++) {
			if (pointsX[i] == point.x() && pointsY[i] == point.y()) {
				point = new MutableVec2(point.x(), point.y() + offset);
				offset += EPSILON;
			}
		}

		if (numPoints == pointsX.length) {
			float af[] = new float[numPoints * 2];
			System.arraycopy(pointsX, 0, af, 0, numPoints);
			pointsX = af;
			af = new float[numPoints * 2];
			System.arraycopy(pointsY, 0, af, 0, numPoints);
			pointsY = af;
		}

		pointsX[numPoints] = point.x();
		pointsY[numPoints] = point.y();
		numPoints++;
	}

	class Triangle {
		int v[];

		Triangle(int i, int j, int k) {
			v = new int[3];
			v[0] = i;
			v[1] = j;
			v[2] = k;
		}
	}

	class Edge {
		int v0, v1, t0, t1;
		boolean suspect;

		Edge() {
			v0 = v1 = t0 = t1 = -1;
		}
	}

	class InternalException extends Exception {
		private static final long serialVersionUID = 4126578960568998025L;

		InternalException(String msg) {
			super(msg);
		}
	}

	@Override
	public int getTriangleCount() {
		return numTriangles;
	}

	@Override
	@Nonnull public IVec2 getTrianglePoint(int tri, int i) {
		float xp = pointsX[triangles[tri].v[i]];
		float yp = pointsY[triangles[tri].v[i]];
		return new MutableVec2(xp, yp);
	}
}