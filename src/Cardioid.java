import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Cardioid extends JFrame
{
	int windowWidth = 1600;
	int windowHeigth = 900;

	int translateWidth = windowWidth / 2;
	int translateHeigth = windowHeigth / 2;

	double radius = 400;
	int num_lines = 300;

	float stepColor = 0.0F;

	long timeStart;

	// -----------------------------------------------------------------------------------------------------------------
	public static void main( String[] args )
	{
		EventQueue.invokeLater( () -> new Cardioid().setVisible( true ) );
	}

	// -----------------------------------------------------------------------------------------------------------------
	public Cardioid()
	{
		super( "Кардиоида" );

		setBounds( 100, 50, 1600, 900 );
		setMinimumSize( new Dimension( 1600, 900 ) );

		setDefaultCloseOperation( EXIT_ON_CLOSE );

		timeStart = System.currentTimeMillis();
	}

	// -----------------------------------------------------------------------------------------------------------------
	@Override
	public void paint( Graphics graphics )
	{
		repaint();

		try
		{
			// Swing имеет проблемы с фпс. поэтому так. костыльно, но как иначе))))
			Thread.sleep( 20 );
		}
		catch( InterruptedException e )
		{}

		graphics.setColor( Color.BLACK );
		graphics.fillRect( 0, 0, this.getWidth(), this.getHeight() );

		long time = getTimeMs();
		radius = 350 + 50 * Math.abs( Math.sin( time * 0.004 ) - 0.5 );
		double factor = 1 + 0.0001 * time;

		for( int i = 0; i < num_lines; i++ )
		{
			double theta = (2 * Math.PI / num_lines) * i;

			int x1 = (int)(radius * Math.cos( theta ) + translateWidth);
			int y1 = (int)(radius * Math.sin( theta ) + translateHeigth);

			int x2 = (int)(radius * Math.cos( factor * theta ) + translateWidth);
			int y2 = (int)(radius * Math.sin( factor * theta ) + translateHeigth);

			graphics.setColor( getColor() );
			graphics.drawLine( x1, y1, x2, y2 );
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	long getTimeMs()
	{
		return System.currentTimeMillis() - timeStart;
	}

	// -----------------------------------------------------------------------------------------------------------------
	Color getColor()
	{
		float[] startHSB = Color.RGBtoHSB( Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), null );
		float[] endHSB = Color.RGBtoHSB( Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), null );

		float brightness = (startHSB[ 2 ] + endHSB[ 2 ]) / 2;
		float saturation = (startHSB[ 1 ] + endHSB[ 1 ]) / 2;

		float hueMax = 0;
		float hueMin = 0;
		if( startHSB[ 0 ] > endHSB[ 0 ] )
		{
			hueMax = startHSB[ 0 ];
			hueMin = endHSB[ 0 ];
		}
		else
		{
			hueMin = startHSB[ 0 ];
			hueMax = endHSB[ 0 ];
		}

		float hue = ((hueMax - hueMin) * stepColor) + hueMin;

		stepColor += 0.00001F;

		// хы, костыль
		if( stepColor == Float.MAX_VALUE )
		{
			stepColor = 0.0F;
		}

		return Color.getHSBColor( hue, saturation, brightness );

	}
}
