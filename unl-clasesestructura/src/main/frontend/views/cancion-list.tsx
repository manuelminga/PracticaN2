import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, DatePicker, Dialog, Grid, GridColumn, GridItemModel, GridSortColumn, TextField, VerticalLayout, HorizontalLayout, Select, Icon } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';

import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import { CancionService } from 'Frontend/generated/endpoints';

import { useEffect, useState } from 'react';
import TipoArchivoEnum from 'Frontend/generated/com/unl/clasesestructura/base/models/TipoArchivoEnum';
import Cancion from 'Frontend/generated/com/unl/clasesestructura/base/models/Cancion';
import Expresion from 'Frontend/generated/com/unl/clasesestructura/base/models/Expresion';
import { order, updateCancion } from 'Frontend/generated/CancionService';

export const config: ViewConfig = {
  title: 'Cancion',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 5,
    title: 'Cancion',
  },
};

type CancionEntryFormProps = {
  onCancionCreated?: () => void;
};

type CancionEntryFormUpdateProps =  {
  onCancionUpdate?: () => void;
  arguments: {
    id: number;
    nombre: string;
    duracion: number;
    url: string;
    tipo: string;
    id_genero?: number;
    id_album?: number;
  };
};

// crear Cancion
function CancionEntryForm(props: CancionEntryFormProps) {
  const dialogOpened = useSignal(false);
  const [tipos, setTipos] = useState<String[]>([]);
  const [generos, setGeneros] = useState<{ id: string, label: string }[]>([]);
  const [albumes, setAlbumes] = useState<{ id: string, label: string }[]>([]);

  const open = () => {
    dialogOpened.value = true;
  };

  const close = () => {
    dialogOpened.value = false;
  };

  const expresion = useSignal('');
  const nombre = useSignal('');
  const duracion = useSignal('');
  const url = useSignal('');
  const tipo = useSignal('');
  const genero = useSignal('');
  const album = useSignal('');

  const createCancion = async () => {
    try {
      console.log('Valores actuales:', {
        nombre: nombre.value,
        duracion: duracion.value,
        url: url.value,
        tipo: tipo.value,
        genero: genero.value,
        album: album.value
      });
      // Validar campos requeridos
      if (!nombre.value.trim() || !duracion.value.trim() || !url.value.trim() ||
        !tipo.value.trim() || !genero.value.trim() || !album.value.trim()) {
        Notification.show('Complete todos los campos requeridos',
          { duration: 5000, position: 'top-center', theme: 'error' });
        return;
      }

      // Validar formato duración
      const durationRegex = /^(\d{1,2}):(\d{2})$/;
      if (!durationRegex.test(duracion.value)) {
        Notification.show('Formato inválido. Use mm:ss (Ejemplo: 03:57)',
          { duration: 5000, position: 'top-center', theme: 'error' });
        return;
      }

      // Convertir a segundos
      const [minutes, seconds] = duracion.value.split(':').map(Number);
      if (seconds >= 60) {
        Notification.show('Los segundos deben ser menores a 60',
          { duration: 5000, position: 'top-center', theme: 'error' });
        return;
      }
      const totalSeconds = minutes * 60 + seconds;

      // Crear canción
      const tipoEnumValue = TipoArchivoEnum[tipo.value as keyof typeof TipoArchivoEnum];
      await CancionService.createCancion(
        nombre.value.trim(),
        totalSeconds.toString(),
        url.value.trim(),
        tipoEnumValue,
        parseInt(genero.value),
        parseInt(album.value)
      );

      // Limpiar y cerrar
      nombre.value = '';
      duracion.value = '';
      url.value = '';
      tipo.value = '';
      genero.value = '';
      dialogOpened.value = false;
      album.value = '';

      // Notificar y refrescar
      Notification.show('Canción creada exitosamente',
        { duration: 5000, position: 'bottom-end', theme: 'success' });
      props.onCancionCreated?.();
    } catch (error) {
      console.error('Error al crear canción:', error);
      Notification.show('Error al crear la canción',
        { duration: 5000, position: 'top-center', theme: 'error' });
      handleError(error);
    }
  };

  // Cargar tipos de archivo y géneros
  useEffect(() => {
    CancionService.listTipoArchivo()
      .then((result) => setTipos((result || []).filter((t): t is string => t !== undefined)))
      .catch(console.error);
    CancionService.listaAlbumGenero()
      .then((result) => setGeneros(result.map(g => ({ id: g.id, label: g.label }))))
      .catch(console.error);
    CancionService.ListaAlbumCombo()
      .then((result) => setAlbumes(result.map(a => ({ id: a.id, label: a.label }))))
      .catch(console.error);
  }, []);

  return (
    <>
      <Dialog
        aria-label="Registrar Canción"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(e) => dialogOpened.value = e.detail.value}
        header={
          <h2 style={{
            flex: 1,
            cursor: 'move',
            margin: 0,
            fontSize: '1.5em',
            fontWeight: 'bold',
            padding: 'var(--lumo-space-m) 0',
          }}>
            Registrar Canción
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close} className="btn-azulito">Cancelar</Button>
            <Button theme="primary" onClick={createCancion} className="btn-gris">
              Registrar
            </Button>
          </>
        )}
      >
        <VerticalLayout style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}>
          <TextField
            label="Nombre"
            placeholder="Nombre de la canción"
            value={nombre.value}
            onValueChanged={(e) => nombre.value = e.detail.value}
            required
          />

          <TextField
            label="Duración mm:ss"
            placeholder="Ejemplo: 03:57"
            value={duracion.value}
            onValueChanged={(e) => duracion.value = e.detail.value}
            pattern="^\d{1,2}:\d{2}$"
            errorMessage="Formato: mm:ss | Ejemplo '3:57'"
            required
          />

          <TextField
            label="URL"
            placeholder="URL del archivo"
            value={url.value}
            onValueChanged={(e) => url.value = e.detail.value}
            required
          />

          <ComboBox
            label="Tipo de archivo"
            items={tipos}
            value={tipo.value}
            onValueChanged={(e) => tipo.value = e.detail.value}
            placeholder="Seleccione tipo"
            required
          />

          <ComboBox
            label="Género"
            items={generos}
            itemValuePath="id"
            itemLabelPath="label"
            value={genero.value}
            onValueChanged={(e) => {
              console.log('Género seleccionado:', e.detail.value);
              genero.value = e.detail.value;
            }}
            placeholder="Seleccione género"
            required
          />
          <ComboBox
            label="Álbum"
            items={albumes}
            itemValuePath="id"
            itemLabelPath="label"
            value={album.value}
            onValueChanged={(e) => album.value = e.detail.value}
            placeholder="Seleccione álbum"
            required
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open} theme="primary" className="btn-black">
        ＋ Registrar Canción
      </Button>
    </>
  );
}

////******************Editar Cancion*********** */

function CancionEntryFormUpdate(props: CancionEntryFormUpdateProps) {
  const dialogOpened = useSignal(false);
  const [tipos, setTipos] = useState<String[]>([]);
  const [generos, setGeneros] = useState<{ id: string, label: string }[]>([]);
  const [albumes, setAlbumes] = useState<{ id: string, label: string }[]>([]);


  const open = () => {
    dialogOpened.value = true;
  };

  const close = () => {
    dialogOpened.value = false;
  };

  const initialDuration = props.arguments.duracion
    ? `${Math.floor(props.arguments.duracion / 60)}:${(props.arguments.duracion % 60).toString().padStart(2, '0')}`
    : '';

  const nombre = useSignal(props.arguments.nombre);
  const duracion = useSignal(initialDuration);
  const url = useSignal(props.arguments.url);
  const tipo = useSignal(props.arguments.tipo);
  const genero = useSignal(props.arguments.id_genero?.toString() || '');
  const ident = useSignal(props.arguments.id);
  const album = useSignal(props.arguments.id_album?.toString() || '');


      // Sincroniza los valores cuando cambian las props
    useEffect(() => {
      nombre.value = props.arguments.nombre;
      duracion.value = initialDuration;
      url.value = props.arguments.url;
      tipo.value = props.arguments.tipo;
      genero.value = props.arguments.id_genero?.toString() || '';
      album.value = props.arguments.id_album?.toString() || '';
    }, [props.arguments.nombre, props.arguments.duracion, props.arguments.id_banda]);


  const updateCancion = async () => {
    try {
      if (nombre.value.trim().length > 0 && duracion.value.trim().length > 0 &&
        url.value.trim().length > 0 && tipo.value.trim().length > 0 && genero.value.trim().length > 0 && album.value.trim().length > 0) {

        const durationRegex = /^(\d{1,2}):(\d{2})$/;
        const match = durationRegex.exec(duracion.value);

        if (!match) {
          Notification.show('Formato de duración inválido. Use mm:ss|Ejm 3:57',
            { duration: 5000, position: 'top-center', theme: 'error' });
          return;
        }

        const minutes = parseInt(match[1]);
        const seconds = parseInt(match[2]);
        const totalSeconds = minutes * 60 + seconds;

        const tipoEnumValue = TipoArchivoEnum[tipo.value as keyof typeof TipoArchivoEnum];
        await CancionService.updateCancion(
          parseInt(ident.value),
          nombre.value,
          totalSeconds.toString(),
          url.value,
          tipoEnumValue,
          parseInt(genero.value),
          parseInt(album.value)
        );

        if (props.onCancionUpdate) {
          props.onCancionUpdate();
        }

        dialogOpened.value = false;
        Notification.show('Canción actualizada exitosamente',
          { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo actualizar, faltan datos',
          { duration: 5000, position: 'top-center', theme: 'error' });
      }
    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  useEffect(() => {
    CancionService.listTipoArchivo()
      .then((result) => setTipos((result || []).filter((tipo): tipo is string => tipo !== undefined)))
      .catch(console.error);

    CancionService.listaAlbumGenero()
      .then((result) => setGeneros(result.map(g => ({ id: g.id, label: g.label }))))
      .catch(console.error);

    CancionService.ListaAlbumCombo()
      .then((result) => setAlbumes(result.map(a => ({ id: a.id, label: a.label }))))
      .catch(console.error);
  }, []);


  return (
    <>
      <Dialog
        aria-label="Editar Cancion"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(event) => {
          dialogOpened.value = event.detail.value;
        }}
        header={
          <h2
            className="draggable"
            style={{
              flex: 1,
              cursor: 'move',
              margin: 0,
              fontSize: '1.5em',
              fontWeight: 'bold',
              padding: 'var(--lumo-space-m) 0',
            }}
          >
            Editar Cancion
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close} className="btn-azulito">Cancelar</Button>
            <Button theme="primary" onClick={updateCancion} className="btn-black">
              Actualizar
            </Button>
          </>
        )}
      >
        <VerticalLayout
          theme="spacing"
          style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}
        >
          <VerticalLayout style={{ alignItems: 'stretch' }}>
            <TextField
              label="Nombre"
              placeholder='Ingrese el nombre de la canción'
              value={nombre.value}
              onValueChanged={(evt) => (nombre.value = evt.detail.value)}
            />
            <TextField
              label="Duración"
              placeholder='Ingrese la duración mm:ss'
              value={duracion.value}
              onValueChanged={(evt) => {
                const regex = /^(\d{1,2}):(\d{2})$/;
                if (regex.test(evt.detail.value) || evt.detail.value === '') {
                  duracion.value = evt.detail.value;
                }
              }}
              pattern="^\d{1,2}:\d{2}$"
              errorMessage="Formato inválido. Use mm:ss | Ejemplo 3:57"
            />
            <TextField
              label="URL"
              placeholder='Ingrese la URL de la canción'
              value={url.value}
              onValueChanged={(evt) => (url.value = evt.detail.value)}
            />
            <ComboBox
              label="Tipo de archivo"
              items={tipos}
              value={tipo.value}
              onValueChanged={(e) => (tipo.value = e.detail.value)}
              placeholder="Seleccione tipo de archivo"
            />
            <ComboBox
              label="Género"
              items={generos}
              itemValuePath="id"
              itemLabelPath="label"
              value={genero.value}
              onValueChanged={(e) => (genero.value = e.detail.value)}
              placeholder="Seleccione género"
            />
            <ComboBox
              label="Álbum"
              items={albumes}
              itemValuePath="id"
              itemLabelPath="label"
              value={album.value}
              onValueChanged={(e) => (album.value = e.detail.value)}
              placeholder="Seleccione álbum"
              required
            />
          </VerticalLayout>
        </VerticalLayout>
      </Dialog>
      <Button onClick={open} className="btn-gris">Editar</Button>
    </>
  );
}

const dateFormatter = new Intl.DateTimeFormat(undefined, {
  dateStyle: 'medium',
});




  function view_valid({ item }: { item: Expresion }) {
    return (
      <span>
        {item.isCorrecto ? "Verdadero" : "Falso"}
      </span>
    );
  }


export default function CancionListView() {
  const [items, setItems] = useState([]);
  const callData = () => {
    CancionService.listCancion().then(function (data) {
      setItems(data);
    });
  }
  useEffect(() => {
    callData();
  }, []);

  function link({ item }: { item: Cancion }) {

    return (
      <span>
        <CancionEntryFormUpdate arguments={item} onCancionUpdate={callData}>
        </CancionEntryFormUpdate>
      </span>
    );
  }

    function index({ model }: { model: GridItemModel<Expresion> }) {
    return (
      <span>
        {model.index + 1}
      </span>
    );
  }



  /*   const dataProvider = useDataProvider<Cancion>({
    list: () => CancionService.listAllCancion(),
  }); */

  const order = (event, columnId) => {
    console.log(event);
    const direction = event.detail.direction;
    var dir = (direction === 'asc') ? 1 : 2;
    CancionService.order(columnId, dir).then(function (data) {
      setItems(data);
    });
  }


  // 1. Estado para géneros y álbumes
  const [generos, setGeneros] = useState<{ id: string, label: string }[]>([]);
  const [albumes, setAlbumes] = useState<{ id: string, label: string }[]>([]);

  // 2. Cargar géneros al montar el componente
  useEffect(() => {
    CancionService.listaAlbumGenero()
      .then((result) => setGeneros(result.map(g => ({ id: g.id, label: g.label }))))
      .catch(console.error);
    CancionService.ListaAlbumCombo()
      .then((result) => setAlbumes(result.map(a => ({ id: a.id, label: a.label }))))
      .catch(console.error);
  }, []);

  // 3. Función para obtener el nombre del género y álbum
  const getGeneroLabel = (id_genero: number | string) => {
    const genero = generos.find(g => g.id === id_genero?.toString());
    return genero ? genero.label : id_genero;
  };

  // 4. Función para obtener el nombre del álbum
  const getAlbumLabel = (id_album: number | string) => {
    const album = albumes.find(a => a.id === id_album?.toString());
    return album ? album.label : 'Sin álbum';
  };





  // Función para formatear la duración de segundos a mm:ss
  const formatDuration = (seconds: number) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  };

  
  // Variables reactivas para la búsqueda
/**
 * criterio: Signal que almacena el atributo seleccionado para buscar (por ejemplo, 
 * 'nombre' o 'genero').
 * texto: Signal que almacena el texto ingresado por el usuario para buscar.
 */
  const criterio = useSignal('');
  const texto = useSignal('');


  /**
 * itemSelect: Opciones disponibles para el campo de selección de criterio de búsqueda.
 * Cada opción tiene una etiqueta (label) y un valor (value) que corresponde 
 * al atributo de la canción.
 */
  const itemSelect = [
    {
      label: 'Nombre',
      value: 'nombre'
    },
    {
      label: 'Género',
      value: 'genero'
    }
  ];


/**
 * search: Función asíncrona que realiza la búsqueda de canciones 
 * según el criterio y texto ingresados.

 */
const search = async () => {
  try {
    CancionService.search(criterio.value, texto.value, 0).then(function (data) {
      setItems(data.data); // Actualiza solo con la lista de resultados

      if (data.message) {
        Notification.show(data.message, {
          duration: 5000,
          position: 'bottom-center',
          theme: 'error'
        });
      } else {
        Notification.show('Datos encontrados', {
          duration: 5000,
          position: 'bottom-center',
          theme: 'success'
        });
      }
    });

/*       // Limpiar y cerrar
      criterio.value = '';
      texto.value = ''; */


    } catch (error) {
      console.error('Error al buscar canciones:', error);
      Notification.show('Error al buscar canciones',
        { duration: 5000, position: 'top-center', theme: 'error' });
      handleError(error);
    }
  };

  return (

    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Canciones">
        <Group>
          <CancionEntryForm onCancionCreated={callData}/>
        </Group>
      </ViewToolbar>
      
      <HorizontalLayout theme="spacing" style={{ justifyContent: 'center', alignItems: 'center', width: '100%' }}>
        <Select items={itemSelect}
          value={criterio.value}
          onValueChanged={(evt) => (criterio.value = evt.detail.value)}
          placeholder={'Seleccione'}>
        </Select>
        <TextField
          placeholder="Ingrese texto a buscar"
          style={{ width: '30%' }}
          value={texto.value}
          onValueChanged={(evt) => (texto.value = evt.detail.value)}
          onKeyDown={(evt) => {
            if (evt.key === 'Enter') {
              search();
            }
          }}


        >
          <Icon slot="prefix" icon="vaadin:search" />
        </TextField>
        <Button onClick={search} theme="secondary" className="btn-azulito">
          Buscar
        </Button>
      </HorizontalLayout>
      <Grid items={items}>
        <GridColumn renderer={index} header="Nro" />
        <GridSortColumn
          path={"nombre"}
          header="Nombre"
          onDirectionChanged={(e) => order(e, 'nombre')} />

        {/* 4. Mostrar el nombre del género */}
        <GridSortColumn
          path="genero"
          header="Género"
          onDirectionChanged={(e) => order(e, 'genero')}
          renderer={({ item }) => <span>{getGeneroLabel(item.id_genero)}</span>}
        />


        <GridColumn
          header="Duración"
          renderer={({ item }) => <span>{formatDuration(item.duracion)}</span>}
        />
        <GridColumn path="url" header="Url" />
        
        <GridSortColumn
         path="tipo" 
         header="Tipo de Archivo"
         onDirectionChanged={(e) => order(e, 'tipo')}
         
       />
        <GridSortColumn
          path="album"
          header="Álbum"
          onDirectionChanged={(e) => order(e, 'album')}
          renderer={({ item }) => <span>{getAlbumLabel(item.id_album)}</span>}
        />
        <GridColumn header="Acciones" renderer={link} />
      </Grid>
    </main>
  );
}