import matplotlib;

matplotlib.use("TkAgg")
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation

# L setup
L = 5

# 2d cartesian plane setup
xmin, xmax, ymin, ymax = -1, L + 1, -1, 1
ticks_frequency = 1
fig, ax = plt.subplots(figsize=(10, 10))
plt.ylim(ymin=0)
ax.set(xlim=(xmin - 1, xmax + 1), ylim=(ymin - 1, ymax + 1), aspect='equal')
ax.spines['bottom'].set_position('zero')
ax.spines['left'].set_position('zero')
ax.spines['top'].set_visible(False)
ax.spines['right'].set_visible(False)
ax.set_xlabel('$x$', size=14, labelpad=-24, x=1.03)
ax.set_ylabel('$y$', size=14, labelpad=-21, y=1.02, rotation=0)
x_ticks = np.arange(xmin, xmax + 1, ticks_frequency)
y_ticks = np.arange(ymin, ymax + 1, ticks_frequency)
ax.set_xticks(x_ticks[x_ticks != 0])
ax.set_yticks(y_ticks[y_ticks != 0])
ax.set_xticks(np.arange(xmin, xmax + 1), minor=True)
ax.set_yticks(np.arange(ymin, ymax + 1), minor=True)
ax.grid(which='both', color='grey', linewidth=1, linestyle='-', alpha=0.2)
arrow_fmt = dict(markersize=4, color='black', clip_on=False)

# Create an array of angles from -pi/2 to pi/2
angles = np.linspace(-np.pi/2, np.pi/2, 100)


# Calculate the x and y coordinates of the left semicircle
leftSemiX = -np.cos(angles)
leftSemiY = np.sin(angles)

# Calculate the x and y coordinates of the right semicircle
rightSemiX = np.cos(angles) + L
rightSemiY = np.sin(angles)

# top and bottom lines
commonX = [0, L]
topY = [1, 1]
bottomY = [-1, -1]

# Plot the stadium billiard
ax.plot(leftSemiX, leftSemiY, color='black')
ax.plot(rightSemiX, rightSemiY, color='black')
ax.plot(commonX, topY, color='black')
ax.plot(commonX, bottomY, color='black')

# Read the points from the file
pointsX = []
pointsY = []

file = open('C:\\Users\\Aram.Azatyan\\Desktop\\task3.txt')
read = file.readlines()

for line in read:
    numbers = line.strip().split(':')
    pointsX.append(float(numbers[0]))
    pointsY.append(float(numbers[1]))

# Create a particle as a scatter plot
particle, = ax.plot([], [], 'ro')

# Create a trail as a line plot
trail, = ax.plot([], [], 'b-')

# Keep track of the particle's path
path = []


# Function to update the particle's position
def update_particle(frame):
    num_points = len(pointsX)
    idx1 = frame // 30
    idx2 = (frame // 30) + 1

    if idx2 < num_points:
        x = np.linspace(pointsX[idx1], pointsX[idx2], 30)[frame % 30]
        y = np.linspace(pointsY[idx1], pointsY[idx2], 30)[frame % 30]
        particle.set_data(x, y)
        path.append((x, y))
    else:
        particle.set_data([], [])

    trail.set_data(*zip(*path))

    return particle, trail


# Function to initialize the animation
def init():
    particle.set_data([], [])
    return particle,


# Calculate the total number of frames based on the number of points
num_frames = (len(pointsX) - 1) * 30

# Calculate the desired speed (in milliseconds) for the animation
animation_speed = 100  # Increase for faster speed

# Calculate the interval between frames based on the desired speed
interval = animation_speed / num_frames

# Create the animation
ani = animation.FuncAnimation(fig, update_particle, repeat=False, frames=num_frames, init_func=init, blit=True,
                              interval=interval)

# Display the plot
plt.show()